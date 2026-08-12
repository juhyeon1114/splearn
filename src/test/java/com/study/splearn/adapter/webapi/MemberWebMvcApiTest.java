package com.study.splearn.adapter.webapi;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import com.study.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.study.splearn.application.member.provided.MemberRegister;
import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.application.member.provided.MemberRegisterRequest;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(MemberApi.class)
@MockitoBean(types = MemberRegister.class)
record MemberWebMvcApiTest(
	MockMvcTester mvcTester,
	ObjectMapper objectMapper,
	MemberRegister memberRegister
) {

	@Test
	@DisplayName("멤버 등록")
	void register() {
		var request = MemberFixture.createMemberRegisterRequest();

		when(memberRegister.register(request)).thenReturn(createRegisteredMember(request, 1L));

		assertThat(post(request))
			.hasStatus(HttpStatus.OK)
			.bodyJson()
			.convertTo(MemberRegisterResponse.class)
			.satisfies(response -> {
				assertThat(response.memberId()).isEqualTo(1L);
				assertThat(response.emailAddress()).isEqualTo(request.email());
			});
	}

	@Test
	@DisplayName("요청 본문을 그대로 서비스에 전달한다")
	void registerDelegatesRequest() {
		var request = MemberFixture.createMemberRegisterRequest();

		when(memberRegister.register(any())).thenReturn(createRegisteredMember(request, 1L));

		post(request);

		verify(memberRegister).register(request);
	}

	@Test
	@DisplayName("이메일 형식이 올바르지 않으면 400")
	void registerFailsWithInvalidEmail() {
		var request = new MemberRegisterRequest("invalid-email", "nickname", "password");

		assertThat(post(request)).hasStatus(HttpStatus.BAD_REQUEST);

		verifyNoInteractions(memberRegister);
	}

	@Test
	@DisplayName("닉네임이 너무 짧으면 400")
	void registerFailsWithShortNickname() {
		var request = new MemberRegisterRequest("test@test.com", "nick", "password");

		assertThat(post(request)).hasStatus(HttpStatus.BAD_REQUEST);

		verifyNoInteractions(memberRegister);
	}

	@Test
	@DisplayName("비밀번호가 너무 짧으면 400")
	void registerFailsWithShortPassword() {
		var request = new MemberRegisterRequest("test@test.com", "nickname", "pass");

		assertThat(post(request)).hasStatus(HttpStatus.BAD_REQUEST);

		verifyNoInteractions(memberRegister);
	}

	private MvcTestResult post(MemberRegisterRequest request) {
		return mvcTester.post()
			.uri("/api/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.exchange();
	}

	/**
	 * 등록 직후의 멤버를 흉내낸다. 영속화를 거치지 않으므로 id는 직접 넣어준다.
	 */
	private Member createRegisteredMember(MemberRegisterRequest request, Long id) {
		var member = Member.register(request.toInfo(), MemberFixture.createPasswordEncoder());
		ReflectionTestUtils.setField(member, "id", id);
		return member;
	}

}
