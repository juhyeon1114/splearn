package com.study.splearn.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.study.splearn.application.provided.MemberFinder;
import com.study.splearn.application.provided.MemberRegister;
import com.study.splearn.application.required.EmailSender;
import com.study.splearn.application.required.MemberRepository;
import com.study.splearn.domain.DuplicateEmailException;
import com.study.splearn.domain.Email;
import com.study.splearn.domain.Member;
import com.study.splearn.domain.MemberRegisterRequest;
import com.study.splearn.domain.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {

	private final MemberFinder memberFinder;
	private final MemberRepository memberRepository;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(MemberRegisterRequest memberRegisterRequest) {
		checkDuplicateEmail(memberRegisterRequest);

		var member = Member.register(memberRegisterRequest, passwordEncoder);

		memberRepository.save(member);

		sendWelcomeEmail(member);

		return member;
	}

	@Override
	public Member activate(Long memberId) {
		var member = memberFinder.find(memberId);

		member.activate();

		return memberRepository.save(member);
	}

	private void sendWelcomeEmail(Member member) {
		emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
	}

	private void checkDuplicateEmail(MemberRegisterRequest memberRegisterRequest) {
		memberRepository.findByEmail(new Email(memberRegisterRequest.email()))
			.ifPresent(_ -> {
				throw new DuplicateEmailException();
			});
	}

}
