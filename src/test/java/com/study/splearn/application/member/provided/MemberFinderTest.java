package com.study.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.support.streotype.ApplicationServiceTest;

import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class MemberFinderTest {
	final MemberFinder memberFinder;
	final MemberRegister memberRegister;

	@Test
	@DisplayName("멤버 조회")
	void test2() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		var found = memberFinder.find(member.getId());

		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(member.getId());
	}

	@Test
	@DisplayName("멤버 조회 실패")
	void test1() {
		assertThatThrownBy(() -> memberFinder.find(999L))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
