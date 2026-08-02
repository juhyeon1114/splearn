package com.study.splearn.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

	private static final MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
	private static final PasswordEncoder passwordEncoder = MemberFixture.createPasswordEncoder();

	@Test
	@DisplayName("멤버 생성")
	void test1() {
		var member = Member.register(
			memberRegisterRequest,
			passwordEncoder
		);

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	@DisplayName("멤버 가입 완료")
	void test3() {
		var member = Member.register(
			memberRegisterRequest,
			passwordEncoder
		);

		member.activate();

		assertThat(member.getStatus())
			.isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	@DisplayName("멤버 가입 완료 실패")
	void test12() {
		var member = Member.register(
			memberRegisterRequest,
			passwordEncoder
		);
		member.activate();

		assertThatThrownBy(member::activate)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	@DisplayName("멤버 탈퇴")
	void test132() {
		var member = Member.register(
			memberRegisterRequest,
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
		var member = Member.register(
			memberRegisterRequest,
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
		var member = Member.register(
			memberRegisterRequest,
			passwordEncoder
		);

		assertThat(member.verifyPassword("password", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("wrong", passwordEncoder)).isFalse();
	}

	@Test
	@DisplayName("닉네임 변경")
	void test1298() {
		var member = Member.register(
			memberRegisterRequest,
			passwordEncoder
		);
		member.activate();

		member.changeNickname("newNickname");

		assertThat(member.getNickname()).isEqualTo("newNickname");
	}

	@Test
	@DisplayName("비밀번호 변경")
	void test254() {
		var member = Member.register(
			memberRegisterRequest,
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
		assertThatThrownBy(() -> Member.register(
			new MemberRegisterRequest("invalid", "nickname", "password"),
			passwordEncoder
		)).isInstanceOf(IllegalArgumentException.class);

	}

}
