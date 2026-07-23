package com.study.splearn.application.provided;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.study.splearn.application.MemberService;
import com.study.splearn.application.required.EmailSender;
import com.study.splearn.application.required.MemberRepository;
import com.study.splearn.domain.Email;
import com.study.splearn.domain.Member;
import com.study.splearn.domain.MemberFixture;
import com.study.splearn.domain.MemberStatus;

class MemberRegisterTest {

	static class MemberRepositoryStub implements MemberRepository {
		@Override
		public Member save(Member member) {
			ReflectionTestUtils.setField(member, "id", 1L);
			return member;
		}
	}

	static class EmailSenderStub implements EmailSender {
		@Override
		public void send(Email email, String subject, String body) {

		}
	}

	static class EmailSenderMock implements EmailSender {
		List<Email> tos = new ArrayList<>();

		@Override
		public void send(Email email, String subject, String body) {
			tos.add(email);
		}
	}

	@Test
	@DisplayName("멤버 등록")
	void test132() {
		var emailSenderMock = new EmailSenderMock();
		var memberRegister = new MemberService(
			new MemberRepositoryStub(),
			emailSenderMock,
			MemberFixture.createPasswordEncoder()
		);

		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

		assertThat(emailSenderMock.tos).size().isEqualTo(1);
		assertThat(emailSenderMock.tos.getFirst()).isEqualTo(member.getEmail());
	}

	@Test
	@DisplayName("멤버 등록 Mockito")
	void test12331() {
		var emailSenderMock = mock(EmailSender.class);
		var memberRegister = new MemberService(
			new MemberRepositoryStub(),
			emailSenderMock,
			MemberFixture.createPasswordEncoder()
		);

		var member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

		verify(emailSenderMock, times(1)).send(any(), any(), any());
	}

}
