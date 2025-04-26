package org.sopt.global.exception;

public class DuplicateTitleException extends CustomException{
	public DuplicateTitleException(){
		super(ErrorCode.DUPLICATE_TITLE);
	}
}
