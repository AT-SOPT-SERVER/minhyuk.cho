package org.sopt.controller.validator;

import org.sopt.dto.request.CommentRequest;
import org.sopt.global.exception.CustomException;
import org.sopt.global.exception.ErrorCode;

public class CommentValidator {

	public static void validComment(String comment){
		if(comment == null || comment.isBlank()){
			throw new CustomException(ErrorCode.WRONG_COMMENT);
		}
	}

}
