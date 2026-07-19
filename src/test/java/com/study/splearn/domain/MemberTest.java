package com.study.splearn.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

	private final MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
		"test@test.com",
		"nickname",
		"password"
	);

	private static final PasswordEncoder passwordEncoder = new PasswordEncoder() {
		@Override
		public String encode(String password) {
			return password.toUpperCase();
		}

		@Override
		public boolean matches(String password, String passwordHash) {
			return encode(password).equals(passwordHash);
		}
	};

	@Test
	@DisplayName("멤버 생성")
	void test1() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	@DisplayName("멤버 가입 완료")
	void test3() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);

		member.activate();

		assertThat(member.getStatus())
			.isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	@DisplayName("멤버 가입 완료 실패")
	void test12() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);
		member.activate();

		assertThatThrownBy(member::activate)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	@DisplayName("멤버 탈퇴")
	void test132() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);
		member.activate();

		member.deactivate();

		assertThat(member.getStatus())
			.isEqualTo(MemberStatus.DEACTIVATED);
	}

	@Test
	@DisplayName("멤버 탈퇴 실패")
	void test123() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);

		assertThatThrownBy(member::deactivate)
			.isInstanceOf(IllegalStateException.class);

		member.activate();
		member.deactivate();

		assertThatThrownBy(member::deactivate)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	@DisplayName("비밀번호 검증")
	void test129() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);

		assertThat(member.verifyPassword("password", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("wrong", passwordEncoder)).isFalse();
	}

	@Test
	@DisplayName("닉네임 변경")
	void test1298() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);
		member.activate();

		member.changeNickname("newNickname");

		assertThat(member.getNickname()).isEqualTo("newNickname");
	}

	@Test
	@DisplayName("비밀번호 변경")
	void test254() {
		var member = Member.create(
			memberCreateRequest,
			passwordEncoder
		);
		member.activate();

		member.changePassword("newPassword", passwordEncoder);

		assertThat(member.verifyPassword("newPassword", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("password", passwordEncoder)).isFalse();
	}

	@Test
	@DisplayName("올바르지 않은 이메일")
	void test0123() {
		assertThatThrownBy(() -> Member.create(
			new MemberCreateRequest("invalid", "nickname", "password"),
			passwordEncoder
		)).isInstanceOf(IllegalArgumentException.class);

	}

}
