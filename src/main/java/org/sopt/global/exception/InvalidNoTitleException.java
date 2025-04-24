package org.sopt.global.exception;

public class InvalidNoTitleException extends CustomException{

	public InvalidNoTitleException(){
		super(ErrorCode.NO_TITLE);
	}
}
