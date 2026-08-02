package com.study.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.study.splearn.SplearnTestConfiguration;
import com.study.splearn.domain.member.MemberFixture;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(
	MemberFinder memberFinder,
	MemberRegister memberRegister
) {

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
