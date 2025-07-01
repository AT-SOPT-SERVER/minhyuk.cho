package org.sopt.repository;

import java.util.List;
import java.util.Set;

import org.sopt.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> ,PostLikeRepositoryCustom  {
	void deleteByUserIdAndPostId(Long userId, Long postId);
	// Boolean existsByUserIdAndPostId(Long userId, Long postId);
	@Query("SELECT pl.user.id FROM PostLike pl WHERE pl.post.id = :postId")
	Set<Long> findUserIdsByPostId(@Param("postId") Long postId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM PostLike pl WHERE pl.post.id IN :postIds AND pl.user.id IN :userIds")
	int deleteByPostIdsAndUserIds(@Param("postIds") List<Long> postIds, @Param("userIds") List<Long> userIds);

	@Modifying(clearAutomatically = true)
	@Query(value = "INSERT IGNORE INTO post_like (post_id, user_id) VALUES (:postId, :userId)",
		nativeQuery = true)
	void insertIgnoreDuplicate(@Param("postId") Long postId, @Param("userId") Long userId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "INSERT INTO post_like (post_id, user_id) VALUES (:postId, :userId) " +
		"ON DUPLICATE KEY UPDATE post_id = post_id", nativeQuery = true)
	void saveIgnoreDuplicate(@Param("postId") Long postId, @Param("userId") Long userId);
}
