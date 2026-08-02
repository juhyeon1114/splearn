package com.study.splearn.domain.shared;

import static org.springframework.util.Assert.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Email(
	@Column(length = 150, nullable = false) String emailAddress
) {

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	public Email {
		isTrue(emailAddress.matches(EMAIL_REGEX), "올바르지 않은 이메일 형식입니다.");
	}

}
