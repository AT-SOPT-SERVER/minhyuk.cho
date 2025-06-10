package org.sopt.dto;

import org.sopt.domain.Comment;

public record CommentResponse(

	Long userId,
	String userName,
	String comment
) {

	public static CommentResponse from(Comment comment) {
		return new CommentResponse(
			comment.getId(),
			comment.getContent(),
			comment.getUser().getName()
		);
	}
}
