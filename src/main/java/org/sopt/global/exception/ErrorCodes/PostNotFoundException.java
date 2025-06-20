package org.sopt.global.exception.ErrorCodes;

public class PostNotFoundException extends  CustomException{
	public PostNotFoundException(){
		super(ErrorCode.POST_NOT_FOUND);
	}
}
