package org.sopt.utils;

import org.sopt.dto.ResponseDTO;
import org.sopt.global.response.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
	public static <T> ResponseEntity<ResponseDTO<T>> success(ResponseCode code ,T data){
		return ResponseEntity.status(code.getStatus())
			.body(new ResponseDTO<>(code.getStatus(), code.getMessage(), data));
	}

	public static <T> ResponseEntity<ResponseDTO<Void>> fail(HttpStatus status, String message){
		return ResponseEntity.status(status)
			.body(new ResponseDTO<>(status.value(),message,null));
	}
}
