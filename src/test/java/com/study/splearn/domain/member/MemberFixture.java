package com.study.splearn.domain.member;

public class MemberFixture {

	public static final String MEMBER_NICKNAME = "newNickname";
	public static final String MEMBER_PROFILE_ADDRESS = "profile123";
	public static final String MEMBER_INTRODUCTION = "자기소개";

	public static MemberRegisterRequest createMemberRegisterRequest() {
		return createMemberRegisterRequest("test@test.com");
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

}
