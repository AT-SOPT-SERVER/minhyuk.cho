package org.sopt.controller;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.exception.DuplicateTitleException;
import org.sopt.exception.TImeLimitException;
import org.sopt.global.CheckTime;
import org.sopt.service.PostService;
import org.sopt.utils.EmojiUtil;

public class PostController {

	private final PostService postService = new PostService();

	public void createPost(String title) {
		if(title.isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else{
			int titleLength = EmojiUtil.getEmojiLength(title);
			if(titleLength > 30){
				throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
			}else if(!CheckTime.checkTime()){
				throw new TImeLimitException();
			}
		}

		Post post = new Post(title);
		postService.createPost(post);

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
		}else{
			int titleLength = EmojiUtil.getEmojiLength(newTitle);
			if(titleLength > 30){
				throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
			}
		}
		postService.updatePostById(id,newTitle);
		return true;
	}

	public Boolean deletePostById(int id) {
		return postService.deletePostById(id);
	}

	public List<Post> searchPostsByKeyword(String keyword) {
		return postService.findPostsByKeyword(keyword);
	}

	public void printToFile(){
		postService.printToFile();
	}

	public void readFromFile(){
		postService.readFromFile();
	}

}
