package org.sopt.global.exception.ErrorCodes;

public class TImeLimitException extends RuntimeException{
	public TImeLimitException(){
		super("게시글을 작성한지 3분이 되지 않았습니다. 3분 뒤에 작성해주세요.");
	}
}
