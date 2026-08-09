package com.study.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.study.splearn.SplearnTestConfiguration;
import com.study.splearn.domain.member.DuplicateEmailException;
import com.study.splearn.domain.member.DuplicateProfileException;
import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.domain.member.MemberRegisterRequest;
import com.study.splearn.domain.member.MemberStatus;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@Transactional // 개별 테스트 종료 후, 트랜잭션 롤백
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(
	MemberRegister memberRegister
) {

	@Test
	@DisplayName("멤버 등록")
	void test132123() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	@DisplayName("이메일 중복 테스트")
	void test1290() {
		memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
			.isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	@DisplayName("멤버 활성화")
	void test11() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

		memberRegister.activate(member.getId());

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	@DisplayName("멤버 비활성화")
	void test123123() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		memberRegister.activate(member.getId());

		assertThat(member.getDetail().getDeactivatedAt()).isNull();

		memberRegister.deactivate(member.getId());

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
		assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
	}

	@Test
	@DisplayName("멤버 정보 수정")
	void test123123123() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		memberRegister.activate(member.getId());

		memberRegister.updateInfo(member.getId(), MemberFixture.createMemberInfoUpdateRequest());

		assertThat(member.getNickname()).isEqualTo(MemberFixture.MEMBER_NICKNAME);
		assertThat(member.getDetail().getProfile().address()).isEqualTo(MemberFixture.MEMBER_PROFILE_ADDRESS);
		assertThat(member.getDetail().getIntroduction()).isEqualTo(MemberFixture.MEMBER_INTRODUCTION);
	}

	@Test
	@DisplayName("멤버 정보 수정 실패")
	void test12312223() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		var member2 = memberRegister.register(MemberFixture.createMemberRegisterRequest2());

		memberRegister.activate(member.getId());
		memberRegister.activate(member2.getId());

		memberRegister.updateInfo(member.getId(), MemberFixture.createMemberInfoUpdateRequest());

		assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), MemberFixture.createMemberInfoUpdateRequest()))
			.isInstanceOf(DuplicateProfileException.class);

		memberRegister.updateInfo(member.getId(), MemberFixture.createMemberInfoUpdateRequest2());
	}

	@Test
	@DisplayName("멤버 등록 Validation 테스트")
	void test1023() {
		// Invalid emailAddress format
		assertThatThrownBy(() -> memberRegister.register(
			new MemberRegisterRequest("invalid-emailAddress", "password123", "John Doe")
		)).isInstanceOf(ConstraintViolationException.class);
	}

}
