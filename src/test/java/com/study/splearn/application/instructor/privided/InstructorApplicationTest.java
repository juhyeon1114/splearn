package com.study.splearn.application.instructor.privided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.splearn.application.instructor.required.InstructorRepository;
import com.study.splearn.application.member.required.MemberRepository;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.instructor.InstructorStatus;
import com.study.splearn.domain.member.MemberFixture;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@RequiredArgsConstructor
class InstructorApplicationTest {

	final InstructorApplication instructorApplication;
	final InstructorRepository instructorRepository;
	final MemberRepository memberRepository;

	private Instructor preparePendingInstructor() {
		var activeMember = MemberFixture.createActiveMember();
		var member = memberRepository.save(activeMember);

		return instructorApplication.apply(new InstructorApplyRequest(member.getId()));
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
