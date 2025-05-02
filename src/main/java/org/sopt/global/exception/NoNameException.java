package org.sopt.global.exception;

public class NoNameException extends CustomException{
	public NoNameException(){
		super(ErrorCode.NO_NAME);
	}
}
