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

	@Test
	@DisplayName("NPE 테스트")
	void test2() {
		assertThatThrownBy(() -> new Member(
			null,
			"nickname",
			"password"
		)).isInstanceOf(NullPointerException.class);

		assertThatThrownBy(() -> new Member(
			"test@test.com",
			null,
			"password"
		)).isInstanceOf(NullPointerException.class);

		assertThatThrownBy(() -> new Member(
			"test@test.com",
			"nickname",
			null
		)).isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("멤버 가입 완료")
	void test3() {
		var member = new Member(
			"test@test.com",
			"nickname",
			"password"
		);

		member.activate();

		assertThat(member.getStatus())
			.isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	@DisplayName("멤버 가입 완료 실패")
	void test12() {
		var member = new Member(
			"test@test.com",
			"nickname",
			"password"
		);
		member.activate();

		assertThatThrownBy(member::activate)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	@DisplayName("멤버 탈퇴")
	void test132() {
		var member = new Member(
			"test@test.com",
			"nickname",
			"password"
		);
		member.activate();

		member.deactivate();

		assertThat(member.getStatus())
			.isEqualTo(MemberStatus.DEACTIVATED);
	}

	@Test
	@DisplayName("멤버 탈퇴 실패")
	void test123() {
		var member = new Member(
			"test@test.com",
			"nickname",
			"password"
		);

		assertThatThrownBy(member::deactivate)
			.isInstanceOf(IllegalStateException.class);

		member.activate();
		member.deactivate();

		assertThatThrownBy(member::deactivate)
			.isInstanceOf(IllegalStateException.class);
	}

}
