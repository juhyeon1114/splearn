package com.study.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.study.splearn.SplearnTestConfiguration;
import com.study.splearn.domain.member.MemberFixture;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
class MemberAuthenticatorTest {

	@Autowired
	private MemberAuthenticator memberAuthenticator;

	@Autowired
	private MemberRegister memberRegister;

	@Test
	@DisplayName("로그인 성공")
	void test12123123() {
		var memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
		var member = memberRegister.register(memberRegisterRequest);

		member.activate();

		var loggedInMember = memberAuthenticator.login(new MemberLoginRequest(
			memberRegisterRequest.email(),
			memberRegisterRequest.password()
		));

		assertThat(member.getId()).isEqualTo(loggedInMember.getId());
		assertThat(member.getEmail()).isEqualTo(loggedInMember.getEmail());
		assertThat(member.getNickname()).isEqualTo(loggedInMember.getNickname());
	}

	@Test
	@DisplayName("비활성 유저 로그인 실패")
	void test1231231() {
		var memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
		memberRegister.register(memberRegisterRequest);

		assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(
			memberRegisterRequest.email(),
			memberRegisterRequest.password()
		))).isInstanceOf(LoginFailedException.class);
	}

	@Test
	@DisplayName("존재하지 않는 유저 로그인 실패")
	void test1123() {
		var memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
		var member = memberRegister.register(memberRegisterRequest);

		member.activate();

		assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(
			"nonexistent@example.com",
			"somePassword123"
		))).isInstanceOf(LoginFailedException.class);
	}
	
	@Test 
	@DisplayName("비밀번호 불일치 로그인 실패")
	void test5995() {
		var memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
		var member = memberRegister.register(memberRegisterRequest);

		member.activate();

		assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(
			memberRegisterRequest.email(),
			"wrongPassword123"
		))).isInstanceOf(LoginFailedException.class);
	}

}
