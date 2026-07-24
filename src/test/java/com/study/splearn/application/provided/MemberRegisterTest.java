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
import com.study.splearn.domain.MemberStatus;

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

}
