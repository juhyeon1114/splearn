package com.study.splearn.application.enrollment.provided;

import com.study.splearn.domain.enrollment.Enrollment;

/**
 * 수강 신청과 관리를 담당
 */
public interface Enroller {
	Enrollment enroll(Long memberId, Long courseId);

	Enrollment startStudying(Long enrollmentId);

	Enrollment complete(Long enrollmentId);
}
