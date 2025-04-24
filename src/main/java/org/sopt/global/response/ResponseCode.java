package org.sopt.global.response;

public enum ResponseCode {
	POST_CREATED(201,"게시글이 작성되었습니다."),
	POST_UPDATED(200, "게시글이 수정되었습니다."),
	POST_DELETED(200,"게시글이 삭제되었습니다."),
	POST_DETAIL(200, "게시글 상세 조회"),
	POST_ALL(200, "전체 게시글이 조회되었습니다."),
	POST_KEY_SEARCH(200,"키워드로 게시글 검색 성공");

	private final int status;
	private final String message;

	ResponseCode(int status,String message){
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
