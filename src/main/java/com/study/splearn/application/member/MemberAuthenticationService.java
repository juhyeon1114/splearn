package com.study.splearn.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.study.splearn.application.member.provided.LoginFailedException;
import com.study.splearn.application.member.provided.MemberAuthenticator;
import com.study.splearn.application.member.provided.MemberLoginRequest;
import com.study.splearn.application.member.required.MemberRepository;
import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.PasswordEncoder;
import com.study.splearn.domain.shared.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
@Transactional
public class MemberAuthenticationService implements MemberAuthenticator {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member login(MemberLoginRequest request) throws LoginFailedException {
		var member = memberRepository.findByEmail(new Email(request.email()))
			.orElseThrow(LoginFailedException::new);

		if (!member.isActive()) {
			throw new LoginFailedException();
		}

		var verified = member.verifyPassword(request.password(), passwordEncoder);
		if (!verified) {
			throw new LoginFailedException();
		}

		return member;
	}
}
