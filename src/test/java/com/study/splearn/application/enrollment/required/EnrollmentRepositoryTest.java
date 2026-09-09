package com.study.splearn.application.enrollment.required;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.study.splearn.domain.enrollment.Enrollment;
import com.study.splearn.support.test.BaseRepositoryTest;

import lombok.RequiredArgsConstructor;

@DataJpaTest
@RequiredArgsConstructor
class EnrollmentRepositoryTest extends BaseRepositoryTest {
	@Test
	void saveAndFindId() {
		var member = prepareActiveMember();
		var course = preparePublishedCourse();

		var enrollment = Enrollment.enroll(member, course);

		enrollment = enrollmentRepository.save(enrollment);

		assertThat(enrollment.getId()).isNotNull();

		entityManager.flush();
		entityManager.clear();

		var found = enrollmentRepository.findById(enrollment.getId()).orElseThrow();

		assertThat(found).isEqualTo(enrollment);
	}

	@Test
	void findByMemberId() {
		var member1 = prepareActiveMember();
		var member2 = prepareActiveMember();

		var course1_1 = preparePublishedCourse();
		var course1_2 = preparePublishedCourse();
		var course2 = preparePublishedCourse();

		var enrollment1 = prepareEnrollment(member1, course1_1);
		var enrollment2 = prepareEnrollment(member1, course1_2);
		var enrollment3 = prepareEnrollment(member2, course2);

		assertThat(enrollmentRepository.findByMemberId(member1.getId())).contains(enrollment1, enrollment2);
		assertThat(enrollmentRepository.findByMemberId(member2.getId())).contains(enrollment3);
	}

	@Test
	void findByMemberIdAndCourseId() {
		var member1 = prepareActiveMember();
		var member2 = prepareActiveMember();

		var course1 = preparePublishedCourse();
		var course2 = preparePublishedCourse();

		var enrollment1 = prepareEnrollment(member1, course1);
		var enrollment2 = prepareEnrollment(member2, course2);

		assertThat(enrollmentRepository.findByMemberIdAndCourseId(member1.getId(), course1.getId())).contains(enrollment1);
		assertThat(enrollmentRepository.findByMemberIdAndCourseId(member2.getId(), course2.getId())).contains(enrollment2);
	}
}
