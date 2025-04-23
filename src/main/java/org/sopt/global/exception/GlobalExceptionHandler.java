package org.sopt.global.exception;

import org.sopt.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateTitleException.class)
	public ResponseEntity<?> handleDuplicateTitle(DuplicateTitleException e) {
		return ResponseEntity.badRequest().body(
			new ResponseDTO<>(400, e.getMessage(), null));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
		return ResponseEntity.badRequest().body(
			new ResponseDTO<>(400,e.getMessage(),null));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(EntityNotFoundException e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			new ResponseDTO<>(404,e.getMessage(),null)
		);
	}
}
