package com.study.splearn.domain.enrollment;

import org.jspecify.annotations.Nullable;

import com.study.splearn.domain.course.Course;
import com.study.splearn.domain.course.CourseFixture;
import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.MemberFixture;

public class EnrollmentFixture {
	public static Enrollment createEnrollment(
		@Nullable Member member,
		@Nullable Course course
	) {
		return Enrollment.enroll(
			member == null ? MemberFixture.createActiveMember() : member,
			course == null ? CourseFixture.createPublishedCourse() : course
		);
	}

	public static Enrollment createEnrollment() {
		return createEnrollment(null, null);
	}
}
