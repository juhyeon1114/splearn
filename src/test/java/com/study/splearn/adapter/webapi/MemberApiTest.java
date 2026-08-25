package com.study.splearn.adapter.webapi;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import com.study.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.study.splearn.application.member.provided.MemberRegisterRequest;
import com.study.splearn.application.member.required.MemberRepository;
import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.domain.member.MemberStatus;
import com.study.splearn.domain.shared.Email;
import com.study.splearn.support.streotype.WebApiAdapterTest;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@WebApiAdapterTest
@RequiredArgsConstructor
public class MemberApiTest {

	final MockMvcTester mockMvcTester;
	final ObjectMapper objectMapper;
	final MemberRepository memberRepository;

	@Test
	@DisplayName("멤버 등록")
	void register() {
		var request = MemberFixture.createMemberRegisterRequest();

		assertThat(post(request))
			.hasStatus(HttpStatus.OK)
			.bodyJson()
			.convertTo(MemberRegisterResponse.class)
			.satisfies(response -> {
				assertThat(response.memberId()).isNotNull();
				assertThat(response.emailAddress()).isEqualTo(request.email());
			});
	}

	@Test
	@DisplayName("등록한 멤버가 PENDING 상태로 저장된다")
	void registerStoresMember() {
		var request = MemberFixture.createMemberRegisterRequest();

		post(request);

		assertThat(memberRepository.findByEmail(new Email(request.email())))
			.hasValueSatisfying(member -> {
				assertThat(member.getNickname()).isEqualTo(request.nickname());
				assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
			});
	}

	@Test
	@DisplayName("이미 등록된 이메일이면 409")
	void registerFailsWithDuplicateEmail() {
		var request = MemberFixture.createMemberRegisterRequest();

		assertThat(post(request)).hasStatus(HttpStatus.OK);

		assertThat(post(request))
			.hasStatus(HttpStatus.CONFLICT)
			.hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
	}

	@Test
	@DisplayName("이메일 형식이 올바르지 않으면 400")
	void registerFailsWithInvalidEmail() {
		var request = new MemberRegisterRequest("invalid-email", "nickname", "password");

		assertThat(post(request)).hasStatus(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("닉네임이 너무 짧으면 400")
	void registerFailsWithShortNickname() {
		var request = new MemberRegisterRequest("test@test.com", "nick", "password");

		assertThat(post(request)).hasStatus(HttpStatus.BAD_REQUEST);

		assertThat(memberRepository.findByEmail(new Email(request.email()))).isEmpty();
	}

	@Test
	@DisplayName("비밀번호가 너무 짧으면 400")
	void registerFailsWithShortPassword() {
		var request = new MemberRegisterRequest("test@test.com", "nickname", "pass");

		assertThat(post(request)).hasStatus(HttpStatus.BAD_REQUEST);

		assertThat(memberRepository.findByEmail(new Email(request.email()))).isEmpty();
	}

	private MvcTestResult post(MemberRegisterRequest request) {
		return mockMvcTester.post()
			.uri("/api/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.exchange();
	}

}
