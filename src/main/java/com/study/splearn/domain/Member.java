package com.study.splearn.domain;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
	private Email email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	public static Member create(
		MemberCreateRequest memberCreateRequest,
		PasswordEncoder passwordEncoder
	) {
		var member = new Member();

		member.email = new Email(requireNonNull(memberCreateRequest.email()));
		member.nickname = requireNonNull(memberCreateRequest.nickname());
		member.passwordHash = passwordEncoder.encode(requireNonNull(memberCreateRequest.password()));

		member.status = MemberStatus.PENDING;

		return member;
	}

	public void activate() {
		state(this.status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

		this.status = MemberStatus.ACTIVE;
	}

	public void deactivate() {
		state(this.status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

		this.status = MemberStatus.DEACTIVATED;
	}

	public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, this.passwordHash);
	}

	public void changeNickname(String newNickname) {
		this.nickname = requireNonNull(newNickname);
	}

	public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode((requireNonNull(newPassword)));
	}

	public boolean isActive() {
		return this.status == MemberStatus.ACTIVE;
	}
}
