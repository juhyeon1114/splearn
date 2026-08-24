package com.study.splearn.domain.instructor;

import com.study.splearn.application.instructor.privided.InstructorApplyRequest;
import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.MemberFixture;

public class InstructorFixture {

	public static Instructor createPendingInstructor(Member member) {
		return Instructor.apply(member);
	}

	public static Instructor createPendingInstructor() {
		var member = MemberFixture.createActiveMember();

		return Instructor.apply(member);
	}

	public static Instructor createApprovedInstructor() {
		var instructor = createPendingInstructor();
		instructor.approve();
		return instructor;
	}

	public static InstructorApplyRequest createApplyRequest(Member member) {
		return new InstructorApplyRequest(member.getId());
	}

}
