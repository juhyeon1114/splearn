package com.study.splearn.application.member.provided;

import com.study.splearn.domain.member.Member;

/**
 * 회원을 조회한다.
 */
public interface MemberFinder {
	Member find(Long id);
}
