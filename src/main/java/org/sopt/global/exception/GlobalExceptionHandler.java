package org.sopt.global.exception;

import org.sopt.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateTitleException.class)
	public ResponseEntity<?> handleDuplicateTitle(DuplicateTitleException e) {
		return ResponseUtil.fail(HttpStatus.BAD_REQUEST,e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
		return ResponseUtil.fail(HttpStatus.BAD_REQUEST,e.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(EntityNotFoundException e){
		return ResponseUtil.fail(HttpStatus.NOT_FOUND,e.getMessage());
	}
}
