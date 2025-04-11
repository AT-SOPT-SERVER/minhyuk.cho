package org.sopt.exception;

public class DuplicateTitleException extends RuntimeException{
	public DuplicateTitleException(){
		super("중복된 제목입니다.");
	}
}
