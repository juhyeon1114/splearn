package com.study.splearn.application;

import org.springframework.stereotype.Service;

import com.study.splearn.application.provided.MemberRegister;
import com.study.splearn.application.required.EmailSender;
import com.study.splearn.application.required.MemberRepository;
import com.study.splearn.domain.Member;
import com.study.splearn.domain.MemberRegisterRequest;
import com.study.splearn.domain.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

	private final MemberRepository memberRepository;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(MemberRegisterRequest memberRegisterRequest) {

		var member = Member.register(memberRegisterRequest, passwordEncoder);

		memberRepository.save(member);

		emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");

		return member;
	}

}
