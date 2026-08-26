package com.study.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.splearn.domain.member.DuplicateEmailException;
import com.study.splearn.domain.member.DuplicateProfileException;
import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.domain.member.MemberStatus;
import com.study.splearn.support.streotype.ApplicationServiceTest;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class MemberRegisterTest {
	final MemberRegister memberRegister;

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
		var memberRegisterRequest = MemberFixture.createMemberRegisterRequest();

		memberRegister.register(memberRegisterRequest);

		assertThatThrownBy(() -> memberRegister.register(memberRegisterRequest))
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
		var memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
		var memberInfoUpdateRequest = MemberFixture.createMemberInfoUpdateRequest();

		var member = memberRegister.register(memberRegisterRequest);

		memberRegister.activate(member.getId());

		memberRegister.updateInfo(member.getId(), memberInfoUpdateRequest);

		assertThat(member.getNickname()).isEqualTo(memberInfoUpdateRequest.nickname());
		assertThat(member.getDetail().getProfile().address()).isEqualTo(memberInfoUpdateRequest.profileAddress());
		assertThat(member.getDetail().getIntroduction()).isEqualTo(memberInfoUpdateRequest.introduction());
	}

	@Test
	@DisplayName("멤버 정보 수정 실패")
	void test12312223() {
		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		var member2 = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		memberRegister.activate(member.getId());
		memberRegister.activate(member2.getId());

		memberRegister.updateInfo(member.getId(), MemberFixture.createMemberInfoUpdateRequest("duplicate"));

		assertThatThrownBy(
			() -> memberRegister.updateInfo(member2.getId(), MemberFixture.createMemberInfoUpdateRequest("duplicate")))
			.isInstanceOf(DuplicateProfileException.class);

		// 자신의 프로필 주소를 그대로 유지하는 것은 허용
		memberRegister.updateInfo(member.getId(), MemberFixture.createMemberInfoUpdateRequest("duplicate"));
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
