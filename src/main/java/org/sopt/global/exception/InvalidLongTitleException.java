package org.sopt.global.exception;

public class InvalidLongTitleException extends CustomException{
	public InvalidLongTitleException(){
		super(ErrorCode.LONG_TITLE);
	}
}
