package org.sopt.controller;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.exception.DuplicateTitleException;
import org.sopt.exception.TImeLimitException;
import org.sopt.global.CheckTime;
import org.sopt.service.PostService;

public class PostController {
	private PostService postService = new PostService();

	private int postId;


	public void createPost(String title) {
		if(title.isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else if(title.length() > 30){
			throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
		}else if(!CheckTime.checkTime()){
			throw new TImeLimitException();
		}

		Post post = new Post(title);
		if(!postService.createPost(post)){
			throw new DuplicateTitleException();
		}
	}

	public List<Post> getAllPosts(){
		return postService.getAllPosts();
	}

	public Post getPostById(int id) {
		return postService.getPostById(id);
	}

	public Boolean updatePostTitle(int id, String newTitle) {
		if(newTitle.isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else if(newTitle.length() > 30){
			throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
		}
		postService.updatePostById(id,newTitle);
		return true;
	}

	public Boolean deletePostById(int id) {
		return postService.deletePostById(id);
	}

	public List<Post> searchPostsByKeyword(String keyword) {
		return null;
	}

}
