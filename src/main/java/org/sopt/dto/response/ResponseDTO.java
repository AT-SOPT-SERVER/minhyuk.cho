package org.sopt.dto.response;

public class ResponseDTO <T>{
	private final int status;
	private final String message;
	private  T result;

	public ResponseDTO(int status, String message, T result){
		this.status = status;
		this.message = message;
		this.result = result;
	}

	public ResponseDTO(int status,String message){
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public T getResult() {
		return result;
	}
}
