package com.study.splearn.application.instructor.privided;

import java.util.Optional;

import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.member.Member;

/**
 * 강사 조회
 */
public interface InstructorFinder {
	Instructor find(Long instructorId);

	Optional<Instructor> findByMember(Long memberId);

	default Optional<Instructor> findByMember(Member member) {
		return findByMember(member.getId());
	}
}
