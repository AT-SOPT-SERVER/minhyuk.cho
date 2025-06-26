package org.sopt.global.exception;

import org.sopt.global.exception.ErrorCodes.CustomException;
import org.sopt.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomException(CustomException e) {

		log.error(e.getMessage());

		return ResponseUtil.fail(
			e.getErrorCode().getStatus(),
			e.getMessage()
		);
	}

}
