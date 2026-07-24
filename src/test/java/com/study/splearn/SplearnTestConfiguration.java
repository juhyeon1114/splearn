package com.study.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.study.splearn.application.required.EmailSender;
import com.study.splearn.domain.MemberFixture;
import com.study.splearn.domain.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {
	@Bean
	public EmailSender emailSender() {
		return (email, subject, body) -> System.out.printf("email: %s, subject: %s, body: %s", email, subject, body);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return MemberFixture.createPasswordEncoder();
	}
}
