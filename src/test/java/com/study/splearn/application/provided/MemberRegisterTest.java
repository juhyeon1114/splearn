package com.study.splearn.application.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.study.splearn.SplearnTestConfiguration;
import com.study.splearn.domain.DuplicateEmailException;
import com.study.splearn.domain.MemberFixture;
import com.study.splearn.domain.MemberRegisterRequest;
import com.study.splearn.domain.MemberStatus;

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
	@DisplayName("멤버 등록 Validation 테스트")
	void test1023() {
		// Invalid email format
		assertThatThrownBy(() -> memberRegister.register(
			new MemberRegisterRequest("invalid-email", "password123", "John Doe")
		)).isInstanceOf(ConstraintViolationException.class);
	}

}
