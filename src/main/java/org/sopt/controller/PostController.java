package org.sopt.controller;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.dto.PostRequest;
import org.sopt.dto.ResponseDTO;
import org.sopt.global.exception.TImeLimitException;
import org.sopt.global.CheckTime;
import org.sopt.service.PostService;
import org.sopt.utils.EmojiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/contents")
	public ResponseEntity<?> createPost(@RequestBody final PostRequest postRequest) {
		if(postRequest.getTitle().isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else{
			int titleLength = EmojiUtil.getEmojiLength(postRequest.getTitle());
			if(titleLength > 30){
				throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
			}
			// else if(!CheckTime.checkTime()){
			// 	throw new TImeLimitException();
			// }
		}
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ResponseDTO<>(201,"게시글이 작성되었습니다.", postService.createPost(postRequest.getTitle())));
	}


	@GetMapping("/contents")
	public ResponseEntity<?> getAllPosts(){
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ResponseDTO<>(200, "전체 게시글이 조회되었습니다.",postService.getAllPosts()));
	}

	// public Post getPostById(int id) {
	// 	return postService.getPostById(id);
	// }
	//
	// public Boolean updatePostTitle(int id, String newTitle) {
	// 	if(newTitle.isEmpty()){
	// 		throw new IllegalArgumentException("제목이 비어있습니다.");
	// 	}else{
	// 		int titleLength = EmojiUtil.getEmojiLength(newTitle);
	// 		if(titleLength > 30){
	// 			throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
	// 		}
	// 	}
	// 	postService.updatePostById(id,newTitle);
	// 	return true;
	// }
	//
	// public Boolean deletePostById(int id) {
	// 	return postService.deletePostById(id);
	// }
	//
	// public List<Post> searchPostsByKeyword(String keyword) {
	// 	return postService.findPostsByKeyword(keyword);
	// }


}
