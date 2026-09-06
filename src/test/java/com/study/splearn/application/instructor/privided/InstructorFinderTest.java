package com.study.splearn.application.instructor.privided;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.splearn.support.streotype.ApplicationServiceTest;
import com.study.splearn.support.test.BaseApplicationServiceTest;

import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorFinderTest extends BaseApplicationServiceTest {
	final InstructorFinder instructorFinder;
	final InstructorApplication instructorApplication;

	@Test
	@DisplayName("findByMember")
	void fsad() {
		var member = prepareMember();

		var instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

		var found = instructorFinder.findByMember(member.getId()).orElseThrow();

		assertThat(found).isEqualTo(instructor);

		assertThat(instructorFinder.findByMember(Long.MAX_VALUE).isPresent()).isFalse();
	}

}
