package com.study.splearn.adapter.email;

import org.springframework.stereotype.Component;

import com.study.splearn.application.member.required.EmailSender;
import com.study.splearn.domain.shared.Email;

import lombok.extern.slf4j.Slf4j;

/**
 * 실제 메일 발송 연동 전까지 로그만 남기는 임시 구현체.
 */
@Slf4j
@Component
public class DummyEmailSender implements EmailSender {

	@Override
	public void send(Email email, String subject, String body) {
		log.info("이메일 발송: {}, 제목: {}, 본문: {}", email, subject, body);
	}
}
