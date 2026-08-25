package com.study.splearn.application.instructor;

import java.util.Optional;

import com.study.splearn.application.instructor.privided.InstructorFinder;
import com.study.splearn.application.instructor.required.InstructorRepository;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.support.stereotype.ApplicationService;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class InstructorQueryService implements InstructorFinder {
	private final InstructorRepository instructorRepository;

	@Override
	public Instructor find(Long instructorId) {
		return instructorRepository.findById(instructorId)
			.orElseThrow(() -> new IllegalArgumentException("강사를 찾을 수 없습니다. instructorId=" + instructorId));
	}

	@Override
	public Optional<Instructor> findByMember(Long memberId) {
		return instructorRepository.findByMemberId(memberId);
	}
}
