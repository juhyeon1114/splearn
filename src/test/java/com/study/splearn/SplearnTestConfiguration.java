package com.study.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.study.splearn.application.member.required.EmailSender;
import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.domain.member.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {
	@Bean
	public EmailSender emailSender() {
		return (email, subject, body) -> System.out.printf("emailAddress: %s, subject: %s, body: %s", email, subject, body);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return MemberFixture.createPasswordEncoder();
	}
}
