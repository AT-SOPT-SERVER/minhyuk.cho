package org.sopt.dto;

import java.util.ArrayList;
import java.util.List;

import org.sopt.domain.Post;
import org.sopt.domain.User;

public class PostListDTO {
	private  final List<Content> contentList;


	public PostListDTO(List<Post> list){
		List<Content> contentList = new ArrayList<>();
		for(Post post : list){
			contentList.add(new Content(post.getUser(), post.getTitle()));
		}
		this.contentList =contentList;
	}

	public List<Content> getContentList() {
		return contentList;
	}

	private class Content{
		private String userName;
		private String title;

		public Content(User user , String title){
			this.userName = user.getName();
			this.title = title;
		}

		public String getUserName(){
			return userName;
		}

		public String getTitle() {
			return title;
		}
	}
}
