package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.domain.PostLike;
import org.sopt.domain.User;

public interface PostLikeRepositoryCustom {
	boolean existsByUserAndPost(User user, Post post);
	PostLike findByUserAndPost(User user, Post post);
}
