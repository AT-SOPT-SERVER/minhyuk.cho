package org.sopt.global.exception.ErrorCodes;

public class InvalidIdException extends CustomException{
	public InvalidIdException(){
		super(ErrorCode.ID_NOT_FOUND);
	}
}
