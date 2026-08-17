package com.study.splearn.application.member.provided;

import com.study.splearn.domain.member.Member;

import jakarta.validation.Valid;

/**
 * 회원 인증
 */
public interface MemberAuthenticator {
	Member login(@Valid MemberLoginRequest request) throws LoginFailedException;
}
