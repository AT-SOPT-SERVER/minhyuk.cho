package org.sopt.repository;

import org.sopt.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> , CommentLikeRepositoryCustom {
}
