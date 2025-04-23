package org.sopt.dto;

import org.sopt.domain.Post;

public class PostDTO {
	private Long contentId;
	private String title;

	public PostDTO(Post post){
		this.contentId = post.getId();
		this.title = post.getTitle();
	}

	public Long getContentId() {
		return contentId;
	}

	public String getTitle() {
		return title;
	}
}
