package org.sopt.repository;

import org.sopt.domain.Comment;
import org.sopt.domain.CommentLike;
import org.sopt.domain.Post;
import org.sopt.domain.User;

public interface CommentLikeRepositoryCustom {
	boolean existsByUserAndComment(User user, Comment comment);
	CommentLike findByUserAndComment(User user , Comment comment);
}
