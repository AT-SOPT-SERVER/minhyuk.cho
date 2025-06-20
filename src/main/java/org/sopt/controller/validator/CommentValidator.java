package org.sopt.controller.validator;

import org.sopt.global.exception.ErrorCodes.CustomException;
import org.sopt.global.exception.ErrorCodes.ErrorCode;

public class CommentValidator {

	public static void validComment(String comment){
		if(comment == null || comment.isBlank()){
			throw new CustomException(ErrorCode.WRONG_COMMENT);
		}
	}

}
