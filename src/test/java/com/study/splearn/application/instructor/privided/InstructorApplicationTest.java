package com.study.splearn.application.instructor.privided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.splearn.application.instructor.required.InstructorRepository;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.instructor.InstructorFixture;
import com.study.splearn.domain.instructor.InstructorStatus;
import com.study.splearn.support.streotype.ApplicationServiceTest;
import com.study.splearn.support.test.BaseApplicationServiceTest;

import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorApplicationTest extends BaseApplicationServiceTest {
	final InstructorApplication instructorApplication;
	final InstructorRepository instructorRepository;

	private Instructor preparePendingInstructor() {
		var member = prepareMember();

		return instructorApplication.apply(InstructorFixture.createApplyRequest(member));
	}

	@Test
	@DisplayName("apply")
	void test123123() {
		var instructor = preparePendingInstructor();

		assertThat(instructor.getId()).isNotNull();
		assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

		var foundInstructor = instructorRepository.findById(instructor.getId()).orElseThrow();

		assertThat(foundInstructor.getId()).isEqualTo(instructor.getId());
	}

	@Test
	@DisplayName("duplicate apply")
	void afdk() {
		var member = prepareMember();

		instructorApplication.apply(InstructorFixture.createApplyRequest(member));

		assertThatThrownBy(() -> instructorApplication.apply(InstructorFixture.createApplyRequest(member)))
			.isInstanceOf(DuplicateInstructorApplicationException.class);
	}

	@Test
	@DisplayName("approve")
	void test1231() {
		var instructor = preparePendingInstructor();

		instructorApplication.approve(instructor.getId());

		var foundInstructor = instructorRepository.findById(instructor.getId()).orElseThrow();

		assertThat(foundInstructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
	}

	@Test
	@DisplayName("reject")
	void fsadlk() {
		var instructor = preparePendingInstructor();

		instructorApplication.reject(instructor.getId());

		var foundInstructor = instructorRepository.findById(instructor.getId()).orElseThrow();

		assertThat(foundInstructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
	}

}
