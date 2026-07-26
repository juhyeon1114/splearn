package com.study.splearn.application.provided;

import com.study.splearn.domain.Member;
import com.study.splearn.domain.MemberRegisterRequest;

import jakarta.validation.Valid;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {

	Member register(@Valid MemberRegisterRequest memberRegisterRequest);

	Member activate(Long memberId);

}
