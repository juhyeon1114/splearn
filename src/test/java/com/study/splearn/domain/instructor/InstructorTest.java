package com.study.splearn.domain.instructor;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.splearn.domain.member.MemberFixture;

class InstructorTest {

	@Test
	@DisplayName("apply")
	void test123() {
		var member = MemberFixture.createActiveMember();

		var instructor = InstructorFixture.createPendingInstructor(member);

		assertThat(instructor.getMember()).isEqualTo(member);
		assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);
	}

	@Test
	@DisplayName("apply failed")
	void test123123() {
		var member = MemberFixture.createPendingMember();

		assertThatThrownBy(() -> Instructor.apply(member))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("등록 완료 상태가 아닌 회원은 강사 신청을 할 수 없습니다.");
	}

	@Test
	@DisplayName("approve")
	void test123132132() {
		var instructor = InstructorFixture.createApprovedInstructor();

		assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
	}

	@Test
	@DisplayName("approve failed")
	void test1001() {
		var instructor = InstructorFixture.createApprovedInstructor();

		assertThatThrownBy(instructor::approve)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	@DisplayName("reject")
	void test121212() {
		var instructor = InstructorFixture.createPendingInstructor();

		instructor.reject();

		assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
	}

	@Test
	@DisplayName("reject failed")
	void test123123123() {
		var instructor = InstructorFixture.createPendingInstructor();

		instructor.reject();

		assertThatThrownBy(instructor::reject)
			.isInstanceOf(IllegalStateException.class);
	}

}
