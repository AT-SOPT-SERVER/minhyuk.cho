package org.sopt.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
	ID_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 ID입니다."),
	DUPLICATE_TITLE(HttpStatus.BAD_REQUEST,"중복된 제목입니다."),
	NO_TITLE(HttpStatus.BAD_REQUEST,"제목이 비어있습니다."),
	LONG_TITLE(HttpStatus.BAD_REQUEST,"제목의 최대 길이는 30자 입니다."),
	NO_LIST(HttpStatus.BAD_REQUEST,"게시글이 존재하지 않습니다.");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message){
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
