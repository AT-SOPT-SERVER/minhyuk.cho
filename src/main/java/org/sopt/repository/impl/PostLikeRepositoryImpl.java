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

	QPostLike like = QPostLike.postLike;

	@Override
	public boolean existsByUserIdAndPostId(Long user, Long post) {

		return queryFactory
			.selectOne()
			.from(like)
			.where(
				like.user.id.eq(user),
				like.post.id.eq(post)
			)
			.fetchFirst() != null;
	}

	@Override
	public PostLike findByUserAndPost(User user, Post post) {

		return queryFactory
			.selectFrom(like)
			.where(
				like.user.eq(user),
				like.post.eq(post)
			)
			.fetchFirst();
	}

	@Override
	public Long countByPost(Post post){
		return queryFactory
			.select(like.count())
			.from(like)
			.where(like.post.eq(post))
			.fetchOne();
	}

	// public Boolean existByUserIdAndPostId(Long userId, Long postId){
	//
	// 	return queryFactory
	// 		.selectOne()
	// 		.from(like)
	// 		.where(like.eq())
	//
	// }

}
