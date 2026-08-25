package com.study.splearn.application.member;

import com.study.splearn.application.member.provided.MemberFinder;
import com.study.splearn.application.member.required.MemberRepository;
import com.study.splearn.domain.member.Member;
import com.study.splearn.support.stereotype.ApplicationService;

import lombok.RequiredArgsConstructor;

@ApplicationService(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

	private final MemberRepository memberRepository;

	@Override
	public Member find(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + memberId));
	}
}
