package org.sopt.dto.response;

import org.sopt.domain.Post;
import org.springframework.data.domain.Page;

public record PostListResponse(Page<Post> posts) {
}
