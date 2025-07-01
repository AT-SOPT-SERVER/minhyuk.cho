package org.sopt.scheduler;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import org.sopt.domain.Post;
import org.sopt.domain.PostLike;
import org.sopt.domain.User;
import org.sopt.repository.PostLikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.sopt.service.post.PostService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Component
@RequiredArgsConstructor
@Slf4j
public class PostLikeSyncScheduler {

	private final RedisTemplate<String, Object> redisTemplate;
	private final PostLikeRepository postLikeRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final PostService postService;

	private static final String LOCK_KEY = "postlike:sync:lock";
	private static final String ADD_PATTERN = "postlike:sync:add:*";
	private static final String REMOVE_PATTERN = "postlike:sync:remove:*";
	private static final int BATCH_SIZE = 1000;

	@Scheduled(cron = "0 */1 * * * *")
	public void sync() {
		// 1. 분산 락 획득
		if (!acquireDistributedLock()) {
			log.debug("다른 인스턴스에서 동기화 진행 중");
			return;
		}

		try {
			log.info("PostLike 동기화 시작");

			// 2. 좋아요 추가/삭제 동시 처리
			syncPostLikes();

			log.info("PostLike 동기화 완료");
		} catch (Exception e) {
			log.error("PostLike 동기화 중 오류 발생", e);
		} finally {
			// 3. 락 해제
			releaseDistributedLock();
		}
	}

	/**
	 * 외부에서 강제로 동기화 실행 (주로 종료 시 사용)
	 */
	public void forceSync() {
		try {
			syncPostLikes();
		} catch (Exception e) {
			log.error("강제 동기화 실패", e);
			throw e;
		}
	}

	private boolean acquireDistributedLock() {
		return Boolean.TRUE.equals(
			redisTemplate.opsForValue()
				.setIfAbsent(LOCK_KEY, "locked", Duration.ofMinutes(10))
		);
	}

	private void releaseDistributedLock() {
		redisTemplate.delete(LOCK_KEY);
	}

	@Transactional
	public void syncPostLikes() {
		// 동기화할 데이터가 있는지 먼저 체크
		Set<String> addKeys = redisTemplate.keys(ADD_PATTERN);
		Set<String> removeKeys = redisTemplate.keys(REMOVE_PATTERN);

		if (addKeys != null && !addKeys.isEmpty()) {
			log.info("📦 [추가 대상 Redis 키 목록]");
			for (String key : addKeys) {
				log.info("🔑 추가 키: {}", key);
			}
		}

		if (removeKeys != null && !removeKeys.isEmpty()) {
			log.info("🧹 [삭제 대상 Redis 키 목록]");
			for (String key : removeKeys) {
				log.info("🔑 삭제 키: {}", key);
			}
		}


		boolean hasAddData = addKeys != null && !addKeys.isEmpty();
		boolean hasRemoveData = removeKeys != null && !removeKeys.isEmpty();

		if (!hasAddData && !hasRemoveData) {
			log.debug("동기화할 데이터가 없습니다.");
			return;
		}

		log.info("동기화 대상 - 추가: {}, 삭제: {}",
			hasAddData ? addKeys.size() : 0,
			hasRemoveData ? removeKeys.size() : 0);

		// 병렬 처리로 성능 최적화
		List<Runnable> tasks = Arrays.asList(
			this::processAddRequests,
			this::processRemoveRequests
		);

		tasks.parallelStream().forEach(Runnable::run);
	}

	private void processAddRequests() {
		Set<String> addKeys = redisTemplate.keys(ADD_PATTERN);
		if (addKeys == null || addKeys.isEmpty()) {
			return;
		}

		Map<Long, Set<Long>> postUserMap = new HashMap<>();

		// 1. Redis 데이터 수집 및 즉시 삭제 (원자적 처리)
		for (String key : addKeys) {
			try {
				Long postId = extractPostId(key, "postlike:sync:add:");
				Set<Object> userIds = redisTemplate.opsForSet().members(key);

				// 즉시 삭제로 중복 처리 방지
				redisTemplate.delete(key);

				if (userIds != null && !userIds.isEmpty()) {
					Set<Long> userIdSet = userIds.stream()
						.map(uid -> Long.valueOf(uid.toString()))
						.collect(Collectors.toSet());

					postUserMap.merge(postId, userIdSet, (existing, newSet) -> {
						existing.addAll(newSet);
						return existing;
					});
				}
			} catch (Exception e) {
				log.error("Redis 키 처리 중 오류: {}", key, e);
			}
		}

		// 2. 배치로 DB 처리
		if (!postUserMap.isEmpty()) {
			batchInsertPostLikes(postUserMap);
		}
	}

	private void processRemoveRequests() {
		Set<String> removeKeys = redisTemplate.keys(REMOVE_PATTERN);
		if (removeKeys == null || removeKeys.isEmpty()) {
			return;
		}

		List<PostLikeKey> deleteTargets = new ArrayList<>();

		// 1. Redis 데이터 수집 및 즉시 삭제
		for (String key : removeKeys) {
			try {
				Long postId = extractPostId(key, "postlike:sync:remove:");
				Set<Object> userIds = redisTemplate.opsForSet().members(key);

				// 즉시 삭제
				redisTemplate.delete(key);

				if (userIds != null) {
					for (Object uid : userIds) {
						Long userId = Long.valueOf(uid.toString());
						deleteTargets.add(new PostLikeKey(postId, userId));
					}
				}
			} catch (Exception e) {
				log.error("Redis 키 처리 중 오류: {}", key, e);
			}
		}

		// 2. 배치로 삭제 처리
		if (!deleteTargets.isEmpty()) {
			batchDeletePostLikes(deleteTargets);
		}
	}

	private void batchInsertPostLikes(Map<Long, Set<Long>> postUserMap) {
		List<PostLike> likesToSave = new ArrayList<>();

		for (Map.Entry<Long, Set<Long>> entry : postUserMap.entrySet()) {
			Long postId = entry.getKey();
			Set<Long> userIds = entry.getValue();

			// 🔥 개별 체크로 변경 (더 정확한 중복 체크)
			for (Long userId : userIds) {
				log.info("userId : " + userId);
				// 개별적으로 존재 여부 체크
				if (!postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
					Post post = postRepository.findById(postId).orElse(null);
					User user = userRepository.findById(userId).orElse(null);

					if (post != null && user != null) {
						likesToSave.add(PostLike.builder()
							.post(post)
							.user(user)
							.build());
					}else{
						log.info("둘 중 하나가 없음");
					}
				}
			}
		}

		// 배치 삽입 (UPSERT 방식으로 변경)
		if (!likesToSave.isEmpty()) {
			upsertPostLikes(likesToSave);
			log.info("✅ 좋아요 추가 완료: {} 건", likesToSave.size());
		}
	}

	private void batchDeletePostLikes(List<PostLikeKey> deleteTargets) {
		// 배치 크기만큼 나누어서 삭제
		for (int i = 0; i < deleteTargets.size(); i += BATCH_SIZE) {
			int end = Math.min(i + BATCH_SIZE, deleteTargets.size());
			List<PostLikeKey> batch = deleteTargets.subList(i, end);

			List<Long> postIds = batch.stream().map(PostLikeKey::getPostId).collect(Collectors.toList());
			List<Long> userIds = batch.stream().map(PostLikeKey::getUserId).collect(Collectors.toList());

			int deletedCount = postLikeRepository.deleteByPostIdsAndUserIds(postIds, userIds);
			log.info("✅ 좋아요 삭제 완료: {} 건", deletedCount);
		}
	}

	// UPSERT 방식으로 중복 안전하게 처리
	private void upsertPostLikes(List<PostLike> likesToSave) {
		for (PostLike like : likesToSave) {
			try {
				// ON DUPLICATE KEY IGNORE 방식으로 처리
				postService.save(
					like.getPost().getId(),
					like.getUser().getId()
				);
				log.info("post저장 시 : " + like.getPost().getId());
				log.info("저장 시 : " + like.getUser().getId());

			} catch (Exception e) {
				log.debug("중복 데이터 무시: postId={}, userId={}",
					like.getPost().getId(), like.getUser().getId());
			}
		}
	}

	private Long extractPostId(String key, String prefix) {
		Long val = Long.valueOf(key.replace(prefix, ""));
		log.info("val : " + val);
		return val;
	}

	// 내부 클래스
	private static class PostLikeKey {
		private final Long postId;
		private final Long userId;

		public PostLikeKey(Long postId, Long userId) {
			this.postId = postId;
			this.userId = userId;
		}

		public Long getPostId() { return postId; }
		public Long getUserId() { return userId; }
	}
}