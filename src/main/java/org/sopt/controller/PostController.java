package org.sopt.controller;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.dto.PostRequest;
import org.sopt.exception.DuplicateTitleException;
import org.sopt.exception.TImeLimitException;
import org.sopt.global.CheckTime;
import org.sopt.repository.PostRepository;
import org.sopt.service.PostService;
import org.sopt.utils.EmojiUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/post")
	public void createPost(@RequestBody final PostRequest postRequest) {
		if(postRequest.getTitle().isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else{
			int titleLength = EmojiUtil.getEmojiLength(postRequest.getTitle());
			if(titleLength > 30){
				throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
			}else if(!CheckTime.checkTime()){
				throw new TImeLimitException();
			}
		}
		Post post = new Post(postRequest.getTitle());
		postService.createPost(post);
	}
	@GetMapping("/posts")
	public ResponseEntity<?> getAllPosts(){
		return ResponseEntity.ok(postService.getAllPosts());
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
