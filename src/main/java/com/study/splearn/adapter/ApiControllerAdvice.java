package com.study.splearn.adapter;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.study.splearn.domain.member.DuplicateEmailException;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ProblemDetail handleRuntimeException(RuntimeException e) {
		return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ProblemDetail handleDuplicateEmailException(DuplicateEmailException e) {
		return getProblemDetail(HttpStatus.CONFLICT, e.getMessage());
	}

	private static ProblemDetail getProblemDetail(HttpStatus httpStatus, String message) {
		var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, message);

		problemDetail.setProperty("exception", message);
		problemDetail.setProperty("timestamp", LocalDateTime.now());

		return problemDetail;
	}

}
