package org.sopt.controller;



import org.sopt.dto.PostDTO;
import org.sopt.dto.PostRequest;
import org.sopt.dto.PostUpdateDTO;
import org.sopt.dto.ResponseDTO;
import org.sopt.global.response.ResponseCode;
import org.sopt.service.PostService;
import org.sopt.utils.EmojiUtil;
import org.sopt.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/contents")
@RestController
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("")
	public ResponseEntity<?> createPost(@RequestBody final PostRequest postRequest) {
		if(postRequest.title().isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else{
			int titleLength = EmojiUtil.getEmojiLength(postRequest.title());
			if(titleLength > 30){
				throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
			}
			// else if(!CheckTime.checkTime()){
			// 	throw new TImeLimitException();
			// }
		}
		return ResponseUtil.success(ResponseCode.POST_CREATED,postService.createPost(postRequest));
	}


	@GetMapping("")
	public ResponseEntity<?> getAllPosts(){
		return ResponseUtil.success(ResponseCode.POST_ALL,postService.getAllPosts());
	}

	@GetMapping("/{contentId}")
	public ResponseEntity<?> getPostById(@PathVariable("contentId") final Long id) {
		return ResponseUtil.success(ResponseCode.POST_DETAIL,postService.getPostById(id));
	}

	@PatchMapping("/{contentId}")
	public ResponseEntity<?> updatePostTitle(@PathVariable("contentId") final Long id, @RequestBody final PostRequest postRequest) {
		if(postRequest.title().isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else{
			int titleLength = EmojiUtil.getEmojiLength(postRequest.title());
			if(titleLength > 30){
				throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
			}
		}
		return ResponseUtil.success(ResponseCode.POST_UPDATED,postService.updatePostById(new PostUpdateDTO(id,postRequest)));

	}

	@DeleteMapping("/{contentId}")
	public ResponseEntity<?> deletePostById(@PathVariable("contentId") Long id) {
		postService.deletePostById(id);
		return ResponseUtil.success(ResponseCode.POST_DELETED,null);
	}

	@GetMapping(params = "keyword")
	public ResponseEntity<?> searchPostsByKeyword(@RequestParam String keyword) {
		return ResponseUtil.success(ResponseCode.POST_KEY_SEARCH,postService.findPostsByKeyword(keyword));
	}

}
