package org.sopt.controller.comment;

import org.sopt.controller.validator.CommentValidator;
import org.sopt.dto.request.CommentRequest;
import org.sopt.dto.request.CommentUpdateRequest;
import org.sopt.dto.response.ResponseDTO;
import org.sopt.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

	//자동으로 Bean의 주입을 시키기 위해서 롬복을 써서 했습니다!
	private final CommentService commentService;


	@PostMapping("/comment")
	public ResponseEntity<ResponseDTO<?>> createComment(@RequestHeader final Long userId,
		@RequestBody CommentRequest commentRequest){

		CommentValidator.validComment(commentRequest.comment());
		commentService.createComment(commentRequest,userId);
		return ResponseEntity.ok(new ResponseDTO<>(201,"댓글 등록에 성공하였습니다."));
	}

	@PatchMapping("/comment")
	public ResponseEntity<ResponseDTO<?>> updateComment(@RequestHeader final Long userId,
		@RequestBody CommentUpdateRequest request){

		CommentValidator.validComment(request.content());
		commentService.updateComment(request,userId);
		return ResponseEntity.ok(new ResponseDTO<>(200,"댓글 수정에 성공하였습니다."));
	}




}
