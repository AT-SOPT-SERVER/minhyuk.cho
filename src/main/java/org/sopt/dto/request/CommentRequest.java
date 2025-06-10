package org.sopt.dto.request;

public record CommentRequest(
	Long postId,
	String comment,
	Long userId
) {
}
