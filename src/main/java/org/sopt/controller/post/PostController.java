package org.sopt.controller.post;



import org.sopt.dto.LikeDTO;
import org.sopt.dto.PostDTO;
import org.sopt.dto.PostListDTO;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.PostUpdateDTO;
import org.sopt.dto.response.PostListResponse;
import org.sopt.dto.response.PostResponseDTO;
import org.sopt.dto.response.ResponseDTO;
import org.sopt.global.response.ResponseCode;
import org.sopt.service.post.PostService;
import org.sopt.service.validator.PostValidator;
import org.sopt.utils.ResponseUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
	public ResponseEntity<ResponseDTO<PostResponseDTO>> createPost(
		@RequestHeader final Long userId,
		@RequestBody final PostRequest postRequest) {
		PostValidator.validateTitle(postRequest);
		// if(!CheckTime.checkTime()){
		// 	throw new TImeLimitException();
		// }
		return ResponseUtil.success(ResponseCode.POST_CREATED,postService.createPost(userId,postRequest));
	}


	@GetMapping("")
	public ResponseEntity<ResponseDTO<PostListResponse>> getAllPosts(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
		return ResponseUtil.success(ResponseCode.POST_ALL,postService.getAllPosts(pageable));
	}

	@GetMapping("/{contentId}")
	public ResponseEntity<ResponseDTO<PostDTO>> getPostById(@PathVariable("contentId") final Long id) {
		return ResponseUtil.success(ResponseCode.POST_DETAIL,postService.getPostById(id));
	}

	@PatchMapping("/{contentId}")
	public ResponseEntity<ResponseDTO<PostDTO>> updatePostTitle(
		@RequestHeader  final Long userId,
		@PathVariable("contentId") final Long id,
		@RequestBody final PostRequest postRequest) {
		PostValidator.validateTitle(postRequest);
		return ResponseUtil.success(ResponseCode.POST_UPDATED,postService.updatePostById(userId,new PostUpdateDTO(id,postRequest)));

	}

	@DeleteMapping("/{contentId}")
	public ResponseEntity<ResponseDTO<Void>> deletePostById(
		@RequestHeader final Long userId,
		@PathVariable("contentId") final Long id) {
		postService.deletePostById(userId,id);
		return ResponseUtil.success(ResponseCode.POST_DELETED,null);
	}

	@GetMapping(params = "keyword")
	public ResponseEntity<ResponseDTO<PostListResponse>> searchPostsByKeyword(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
		,@RequestParam String keyword) {
		return ResponseUtil.success(ResponseCode.POST_KEY_SEARCH,postService.findPostsByKeyword(pageable,keyword));
	}


	@PostMapping("/like")
	public ResponseEntity<ResponseDTO<LikeDTO>> createPostLike(@RequestParam Long userId, @RequestParam Long postId){
		return  ResponseUtil.success(ResponseCode.POST_LIKE,postService.createPostLike(userId,postId));
	}


}
