package org.sopt.repository;

import org.sopt.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> ,PostLikeRepositoryCustom  {
}
