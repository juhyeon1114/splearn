package com.study.splearn.application.enrollment.provided;

import java.util.List;
import java.util.Optional;

import com.study.splearn.domain.enrollment.Enrollment;

/**
 * 수강 신청 조회를 담당
 */
public interface EnrollmentFinder {
	Enrollment find(Long enrollmentId);

	List<Enrollment> findByMember(Long memberId);

	Optional<Enrollment> findByMemberAndCourse(Long memberId, Long courseId);
}
