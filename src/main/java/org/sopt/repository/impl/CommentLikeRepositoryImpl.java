package org.sopt.repository.impl;

import org.sopt.domain.Comment;
import org.sopt.domain.CommentLike;
import org.sopt.domain.Post;
import org.sopt.domain.QCommentLike;
import org.sopt.domain.User;
import org.sopt.repository.CommentLikeRepositoryCustom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	QCommentLike like = QCommentLike.commentLike;
	@Override
	public boolean existsByUserAndComment(User user, Comment comment) {

		return queryFactory
			.selectOne()
			.from(like)
			.where(
				like.user.eq(user),
				like.comment.eq(comment)
			)
			.fetchFirst() != null;
	}


	@Override
	public CommentLike findByUserAndComment(User user, Comment comment){

		return queryFactory
			.selectFrom(like)
			.where(
				like.user.eq(user),
				like.comment.eq(comment)
			)
			.fetchFirst();
	}

}
