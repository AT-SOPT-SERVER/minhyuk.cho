package org.sopt.dto.request;

public record CommentUpdateRequest(String content, Long postId,Long commentId) {
}
