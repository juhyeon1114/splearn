package com.study.splearn.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.study.splearn.domain.member.DuplicateEmailException;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DuplicateEmailException.class)
	public ProblemDetail handleDuplicateEmailException(DuplicateEmailException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
	}

}
