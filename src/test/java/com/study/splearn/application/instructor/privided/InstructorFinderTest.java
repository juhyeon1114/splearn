package com.study.splearn.application.instructor.privided;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.splearn.application.member.provided.MemberRegister;
import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.support.streotype.ApplicationServiceTest;

import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorFinderTest {
	final InstructorFinder instructorFinder;
	final InstructorApplication instructorApplication;
	final MemberRegister memberRegister;

	@Test
	@DisplayName("findByMember")
	void fsad() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		member = memberRegister.activate(member.getId());

		var instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

		var found = instructorFinder.findByMember(member.getId()).orElseThrow();

		assertThat(found).isEqualTo(instructor);

		assertThat(instructorFinder.findByMember(Long.MAX_VALUE).isPresent()).isFalse();
	}

}
