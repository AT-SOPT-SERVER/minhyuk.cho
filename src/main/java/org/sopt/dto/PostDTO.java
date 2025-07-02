package org.sopt.dto;

import java.util.List;

import org.sopt.domain.Comment;

public record PostDTO (String title, String content, String userName, long likeCount, List<CommentResponse> comments) {
}
