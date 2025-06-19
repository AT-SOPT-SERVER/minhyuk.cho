package org.sopt.repository.impl;

import org.sopt.domain.Post;
import org.sopt.domain.PostLike;
import org.sopt.domain.QCommentLike;
import org.sopt.domain.QPostLike;
import org.sopt.domain.User;
import org.sopt.repository.PostLikeRepositoryCustom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsByUserAndPost(User user, Post post) {
		QPostLike like = QPostLike.postLike;

		return queryFactory
			.selectOne()
			.from(like)
			.where(
				like.user.eq(user),
				like.post.eq(post)
			)
			.fetchFirst() != null;
	}

	@Override
	public PostLike findByUserAndPost(User user, Post post) {
		QPostLike like = QPostLike.postLike;

		return queryFactory
			.selectFrom(like)
			.where(
				like.user.eq(user),
				like.post.eq(post)
			)
			.fetchFirst();
	}
}
