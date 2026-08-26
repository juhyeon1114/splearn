package com.study.splearn.domain.member;

import org.instancio.Instancio;
import org.instancio.Select;

import com.study.splearn.application.member.provided.MemberRegisterRequest;

public class MemberFixture {

	public static MemberRegisterRequest createMemberRegisterRequest() {
		return createMemberRegisterRequest(Instancio.gen().net().email().get());
	}

	public static MemberRegisterRequest createMemberRegisterRequest(String email) {
		return Instancio.of(MemberRegisterRequest.class)
			.set(Select.field(MemberRegisterRequest::email), email)
			.create();
	}

	public static PasswordEncoder createPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches(String password, String passwordHash) {
				return encode(password).equals(passwordHash);
			}
		};
	}

	public static MemberInfoUpdateRequest createMemberInfoUpdateRequest() {
		return Instancio.of(MemberInfoUpdateRequest.class)
			.generate(
				Select.field(MemberInfoUpdateRequest::profileAddress),
				// Instancio 텍스트 패턴 토큰: #c = 소문자, #d = 숫자
				gen -> gen.text().pattern("#c#c#c#c#c#d#d#d")
			)
			.create();
	}

	public static MemberInfoUpdateRequest createMemberInfoUpdateRequest(String profileAddress) {
		return Instancio.of(MemberInfoUpdateRequest.class)
			.set(Select.field(MemberInfoUpdateRequest::profileAddress), profileAddress)
			.create();
	}

	public static Member createPendingMember() {
		var memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
		return Member.register(memberRegisterRequest.toInfo(), createPasswordEncoder());
	}

	public static Member createActiveMember() {
		var member = createPendingMember();
		member.activate();
		return member;
	}

}
