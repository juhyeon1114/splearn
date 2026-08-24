package com.study.splearn.application.instructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.study.splearn.application.instructor.privided.InstructorFinder;
import com.study.splearn.application.instructor.required.InstructorRepository;
import com.study.splearn.domain.instructor.Instructor;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@Validated
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
		return instructorRepository.findByMember_Id(memberId);
	}
}
