package com.study.splearn.support.stereotype;

import java.util.List;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
	private final List<String> errors;

	public ValidationException(List<String> errors) {
		this.errors = errors;
	}
}
