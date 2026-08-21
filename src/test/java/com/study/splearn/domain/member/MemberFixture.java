package com.study.splearn.domain.member;

import com.study.splearn.application.member.provided.MemberRegisterRequest;

public class MemberFixture {

	public static final String MEMBER_NICKNAME = "newNickname";
	public static final String MEMBER_PROFILE_ADDRESS = "profile123";
	public static final String MEMBER_INTRODUCTION = "자기소개";
	public static final String MEMBER_NICKNAME_2 = "newNickname2";
	public static final String MEMBER_PROFILE_ADDRESS_2 = "profile1232";
	public static final String MEMBER_INTRODUCTION_2 = "자기소개2";

	public static MemberRegisterRequest createMemberRegisterRequest() {
		return createMemberRegisterRequest("test@test.com");
	}

	public static MemberRegisterRequest createMemberRegisterRequest2() {
		return createMemberRegisterRequest("test2@test.com");
	}

	public static MemberRegisterRequest createMemberRegisterRequest(String email) {
		return new MemberRegisterRequest(
			email,
			"nickname",
			"password"
		);
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
		return new MemberInfoUpdateRequest(MEMBER_NICKNAME, MEMBER_PROFILE_ADDRESS, MEMBER_INTRODUCTION);
	}

	public static MemberInfoUpdateRequest createMemberInfoUpdateRequest2() {
		return new MemberInfoUpdateRequest(MEMBER_NICKNAME_2, MEMBER_PROFILE_ADDRESS_2, MEMBER_INTRODUCTION_2);
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
