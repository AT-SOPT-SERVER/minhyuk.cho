package org.sopt.dto.response;

import java.util.List;

import org.sopt.dto.CommentResponse;

public record CommentListResponse(
	List<CommentResponse> comments) {
}
