package com.study.splearn.application.member.provided;

import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.MemberInfoUpdateRequest;

import jakarta.validation.Valid;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {

	Member register(@Valid MemberRegisterRequest memberRegisterRequest);

	Member activate(Long memberId);

	Member deactivate(Long memberId);

	Member updateInfo(Long memberId, @Valid MemberInfoUpdateRequest memberInfoUpdateRequest);

}
