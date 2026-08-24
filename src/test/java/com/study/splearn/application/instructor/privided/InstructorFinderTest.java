package com.study.splearn.application.instructor.privided;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.study.splearn.application.member.provided.MemberRegister;
import com.study.splearn.domain.member.MemberFixture;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
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
