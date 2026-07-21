package com.study.splearn.domain;

import static org.springframework.util.Assert.*;

import jakarta.persistence.Embeddable;

@Embeddable
public record Email(
	String address
) {

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	public Email {
		isTrue(address.matches(EMAIL_REGEX), "올바르지 않은 이메일 형식입니다.");
	}

}
