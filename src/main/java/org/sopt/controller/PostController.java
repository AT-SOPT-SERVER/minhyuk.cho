package org.sopt.controller;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.service.PostService;

public class PostController {
	private PostService postService = new PostService();

	private int postId;


	public void createPost(String title) {
		Post post = new Post(postId++,title);
		postService.createPsot(post);
	}
	public Boolean updatePostTitle(int id, String newTitle) {
		return null;
	}

	public List<Post> getAllPosts(){
		return postService.getAllPosts();
	}

	public Post getPostById(int id) {
		return postService.getPostById(id);
	}

	public Boolean deletePostById(int id) {
		return postService.deletePostById(id);
	}

	public List<Post> searchPostsByKeyword(String keyword) {
		return null;
	}
	// public Object selectMethod(){
	//
	// }
}
