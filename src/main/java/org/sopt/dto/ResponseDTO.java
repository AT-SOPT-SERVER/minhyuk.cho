package org.sopt.dto;

public class ResponseDTO <T>{
	private final int status;
	private final String message;
	private final T result;

	public ResponseDTO(int status, String message, T result){
		this.status = status;
		this.message = message;
		this.result = result;
	}
}
