package org.sopt.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
	ID_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 ID입니다."),
	DUPLICATE_TITLE(HttpStatus.BAD_REQUEST,"중복된 제목입니다."),
	NO_TITLE(HttpStatus.BAD_REQUEST,"제목이 비어있습니다."),
	LONG_TITLE(HttpStatus.BAD_REQUEST,"제목의 최대 길이는 30자 입니다."),
	NO_LIST(HttpStatus.BAD_REQUEST,"게시글이 존재하지 않습니다."),
	NO_NAME(HttpStatus.BAD_REQUEST,"유저의 이름이 존재하지 않습니다."),
	LONG_POST(HttpStatus.BAD_REQUEST,"게시물의 내용은 1000자 이내입니다."),
	LONG_NAME(HttpStatus.BAD_REQUEST,"유저의 이름은 10자 이내입니다."),
	NO_USER(HttpStatus.BAD_REQUEST,"사용자를 찾을 수 없습니다."),
	WRONG_COMMENT(HttpStatus.BAD_REQUEST,"댓글의 최대 길이는 300자 입니다."),
	NO_COMMENT(HttpStatus.BAD_REQUEST,"존재하지 않는 댓글입니다."),
	WRONG_USER(HttpStatus.BAD_REQUEST,"사용자의 댓글이 아닙니다.");

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
