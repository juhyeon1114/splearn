package com.study.splearn.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.study.splearn.application.provided.MemberFinder;
import com.study.splearn.application.required.MemberRepository;
import com.study.splearn.domain.Member;

import lombok.RequiredArgsConstructor;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

	private final MemberRepository memberRepository;

	@Override
	public Member find(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + memberId));
	}
}
