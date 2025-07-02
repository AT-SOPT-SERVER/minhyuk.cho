package org.sopt.controller.comment;

import org.sopt.controller.validator.CommentValidator;
import org.sopt.dto.request.CommentRequest;
import org.sopt.dto.request.CommentUpdateRequest;
import org.sopt.dto.response.ResponseDTO;
import org.sopt.service.comment.CommentService;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

	//자동으로 Bean의 주입을 시키기 위해서 롬복을 써서 했습니다!
	private final CommentService commentService;


	@PostMapping()
	public ResponseEntity<ResponseDTO<?>> createComment(@RequestHeader final Long userId,
		@RequestBody CommentRequest commentRequest){

		CommentValidator.validComment(commentRequest.comment());
		commentService.createComment(commentRequest,userId);
		return ResponseEntity.ok(new ResponseDTO<>(201,"댓글 등록에 성공하였습니다."));
	}

	@PatchMapping()
	public ResponseEntity<ResponseDTO<?>> updateComment(@RequestHeader final Long userId,
		@RequestBody CommentUpdateRequest request){

		CommentValidator.validComment(request.content());
		commentService.updateComment(request,userId);
		return ResponseEntity.ok(new ResponseDTO<>(200,"댓글 수정에 성공하였습니다."));
	}


	@GetMapping("/{commentId}")
	public ResponseEntity<ResponseDTO<?>> getComment(@PathVariable Long commentId){
		return ResponseEntity.ok(new ResponseDTO<>(200,"댓글 조회에 성공하였습니다.",commentService.getComments(commentId)));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ResponseDTO<?>> deleteComment(@PathVariable Long commentId, @RequestHeader Long userId){
		commentService.deleteComment(commentId,userId);
		return ResponseEntity.ok(new ResponseDTO<>(200,"댓글 삭제에 성공하였습니다."));
	}

	@PostMapping("/like")
	public ResponseEntity<ResponseDTO<?>> createLike(@RequestParam Long userId, @RequestParam Long commentId){
		return ResponseEntity.ok(new ResponseDTO<>(200,commentService.createCommentLike(userId,commentId).description()));
	}


}
