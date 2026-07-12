package com.study.splearn.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

	@Test
	@DisplayName("멤버 생성")
	void test1() {
		var member = new Member(
			"test@test.com",
			"nickname",
			"password"
		);

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

}
