package org.sopt.controller.post;



import org.sopt.dto.request.PostRequest;
import org.sopt.dto.PostUpdateDTO;
import org.sopt.global.response.ResponseCode;
import org.sopt.service.post.PostService;
import org.sopt.service.validator.PostValidator;
import org.sopt.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<?> createPost(
		@RequestHeader final Long userId,
		@RequestBody final PostRequest postRequest) {
		PostValidator.validateTitle(postRequest);
		// if(!CheckTime.checkTime()){
		// 	throw new TImeLimitException();
		// }
		return ResponseUtil.success(ResponseCode.POST_CREATED,postService.createPost(userId,postRequest));
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
	public ResponseEntity<?> updatePostTitle(
		@RequestHeader  final Long userId,
		@PathVariable("contentId") final Long id,
		@RequestBody final PostRequest postRequest) {
		PostValidator.validateTitle(postRequest);
		return ResponseUtil.success(ResponseCode.POST_UPDATED,postService.updatePostById(userId,new PostUpdateDTO(id,postRequest)));

	}

	@DeleteMapping("/{contentId}")
	public ResponseEntity<?> deletePostById(
		@RequestHeader final Long userId,
		@PathVariable("contentId") final Long id) {
		postService.deletePostById(userId,id);
		return ResponseUtil.success(ResponseCode.POST_DELETED,null);
	}

	@GetMapping(params = "keyword")
	public ResponseEntity<?> searchPostsByKeyword(@RequestParam String keyword) {
		return ResponseUtil.success(ResponseCode.POST_KEY_SEARCH,postService.findPostsByKeyword(keyword));
	}

}
