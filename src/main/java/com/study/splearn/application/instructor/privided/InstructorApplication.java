package com.study.splearn.application.instructor.privided;

import com.study.splearn.domain.instructor.Instructor;

import jakarta.validation.Valid;

/**
 * 강사 신청
 */
public interface InstructorApplication {
	Instructor apply(@Valid InstructorApplyRequest applyRequest);

	Instructor approve(Long instructorId);

	Instructor reject(Long instructorId);
}
