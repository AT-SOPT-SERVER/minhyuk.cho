package org.sopt.global.exception;

public class NoListException extends  CustomException{
	public NoListException(){
		super(ErrorCode.NO_LIST);
	}
}
