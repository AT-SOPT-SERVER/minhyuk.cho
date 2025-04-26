package org.sopt.dto;

import java.util.ArrayList;
import java.util.List;

import org.sopt.domain.Post;


public class PostListDTO {
	private  final List<Content> contentList;


	public PostListDTO(List<Post> list){
		List<Content> contentList = new ArrayList<>();
		for(Post post : list){
			contentList.add(new Content(post.getId(), post.getTitle()));
		}
		this.contentList =contentList;
	}

	public List<Content> getContentList() {
		return contentList;
	}

	private class Content{
		private Long contentId;
		private String title;

		public Content(Long contentId, String title){
			this.contentId = contentId;
			this.title = title;
		}

		public Long getContentId() {
			return contentId;
		}

		public String getTitle() {
			return title;
		}
	}
}
