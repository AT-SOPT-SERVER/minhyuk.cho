package org.sopt.controller;



import org.sopt.dto.PostRequest;
import org.sopt.dto.ResponseDTO;
import org.sopt.service.PostService;
import org.sopt.utils.EmojiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ResponseDTO<>(201,"게시글이 작성되었습니다.", postService.createPost(postRequest.title())));
	}


	@GetMapping("/contents")
	public ResponseEntity<?> getAllPosts(){
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ResponseDTO<>(200, "전체 게시글이 조회되었습니다.",postService.getAllPosts()));
	}

	@GetMapping("/contents/{contentId}")
	public ResponseEntity<?> getPostById(@PathVariable("contentId") final Long id) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ResponseDTO<>(200,"게시글 상세 조회",postService.getPostById(id)));
	}

	@PatchMapping("contents/{contentId}")
	public ResponseEntity<?> updatePostTitle(@PathVariable("contentId") final Long id, @RequestBody final PostRequest postRequest) {
		if(postRequest.title().isEmpty()){
			throw new IllegalArgumentException("제목이 비어있습니다.");
		}else{
			int titleLength = EmojiUtil.getEmojiLength(postRequest.title());
			if(titleLength > 30){
				throw new IllegalArgumentException("제목의 최대 길이는 30자입니다.");
			}
		}

		return ResponseEntity.status(HttpStatus.OK)
			.body(new ResponseDTO<>(200,"게시글이 수정되었습니다.",postService.updatePostById(id,postRequest.title())));

	}

	@DeleteMapping("/contents/{contentId}")
	public ResponseEntity<?> deletePostById(@PathVariable("contentId") Long id) {
		postService.deletePostById(id);
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ResponseDTO<>(200,"게시글이 삭제되었습니다.",null));
	}

	// public List<Post> searchPostsByKeyword(String keyword) {
	// 	return postService.findPostsByKeyword(keyword);
	// }


}
