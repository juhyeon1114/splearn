package com.study.splearn.domain.enrollment;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.study.splearn.domain.course.CourseFixture;
import com.study.splearn.domain.member.MemberFixture;

class EnrollmentTest {
	@Test
	void enroll() {
		var enrollment = EnrollmentFixture.createEnrollment();

		assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
		assertThat(enrollment.getEnrolledAt()).isNotNull();
	}

	@Test
	void enrollFailNotPublishedCourse() {
		var member = MemberFixture.createActiveMember();
		var course = CourseFixture.createCourse();

		assertThatThrownBy(() -> Enrollment.enroll(member, course))
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void startStudying() {
		var enrollment = EnrollmentFixture.createEnrollment();

		enrollment.startStudying();

		assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);
	}

	@Test
	void complete() {
		var enrollment = EnrollmentFixture.createEnrollment();
		enrollment.startStudying();

		enrollment.complete();

		assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
		assertThat(enrollment.getCompletedAt()).isNotNull();
	}
}
