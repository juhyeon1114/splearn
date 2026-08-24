package com.study.splearn.application.instructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.study.splearn.application.instructor.privided.InstructorApplication;
import com.study.splearn.application.instructor.privided.InstructorApplyRequest;
import com.study.splearn.application.instructor.privided.InstructorFinder;
import com.study.splearn.application.instructor.required.InstructorRepository;
import com.study.splearn.application.member.provided.MemberFinder;
import com.study.splearn.domain.instructor.Instructor;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class InstructorModifyService implements InstructorApplication {
	private final InstructorRepository instructorRepository;
	private final InstructorFinder instructorFinder;
	private final MemberFinder memberFinder;

	@Override
	public Instructor apply(InstructorApplyRequest applyRequest) {
		var member = memberFinder.find(applyRequest.memberId());

		var instructor = Instructor.apply(member);

		return instructorRepository.save(instructor);
	}

	@Override
	public Instructor approve(Long instructorId) {
		var instructor = instructorFinder.findByMember(instructorId)
			.orElseThrow(() -> new IllegalArgumentException("강사 신청 내역이 존재하지 않습니다."));

		instructor.approve();

		return instructorRepository.save(instructor);
	}

	@Override
	public Instructor reject(Long instructorId) {
		var instructor = instructorFinder.find(instructorId);

		instructor.reject();

		return instructorRepository.save(instructor);
	}
}
