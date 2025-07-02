package org.sopt.service.post;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeCacheService {

	private final RedisTemplate<String, Object> redisTemplate;

	// Redis Key 생성 메서드
	private String likeKey(Long postId) {
		return "post:likes:" + postId;
	}

	private String userSetKey(Long postId) {
		return "post:liked_users:" + postId;
	}

	private String syncAddKey(Long postId) {
		return "postlike:sync:add:" + postId;
	}

	private String syncRemoveKey(Long postId) {
		return "postlike:sync:remove:" + postId;
	}

	// 좋아요 여부 확인
	public boolean isLiked(Long postId, Long userId) {
		return Boolean.TRUE.equals(
			redisTemplate.opsForSet().isMember(userSetKey(postId), userId.toString())
		);
	}

	// 좋아요 누르기
	public void like(Long postId, Long userId) {
		redisTemplate.opsForSet().add(userSetKey(postId), userId.toString());         // 유저 등록
		redisTemplate.opsForValue().increment(likeKey(postId));                      // 카운트 증가
		redisTemplate.opsForSet().add(syncAddKey(postId), userId.toString());        // 동기화 목록 추가
		redisTemplate.opsForSet().remove(syncRemoveKey(postId), userId.toString());  // 취소 목록에서 제거
	}

	// 좋아요 취소
	public void unlike(Long postId, Long userId) {
		redisTemplate.opsForSet().remove(userSetKey(postId), userId.toString());
		redisTemplate.opsForValue().decrement(likeKey(postId));
		redisTemplate.opsForSet().add(syncRemoveKey(postId), userId.toString());
		redisTemplate.opsForSet().remove(syncAddKey(postId), userId.toString());
	}

	// 좋아요 수 가져오기
	public int getLikeCount(Long postId) {
		Object val = redisTemplate.opsForValue().get(likeKey(postId));
		return val != null ? (int) val : 0;
	}

	// Redis에 초기값 세팅
	public void initLikeCount(Long postId, int count) {
		redisTemplate.opsForValue().set(likeKey(postId), count);
	}
}
