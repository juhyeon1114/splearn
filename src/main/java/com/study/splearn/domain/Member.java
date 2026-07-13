package com.study.splearn.domain;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import lombok.Getter;

@Getter
public class Member {
	private String email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	private Member(String email, String nickname, String passwordHash) {
		this.email = requireNonNull(email);
		this.nickname = requireNonNull(nickname);
		this.passwordHash = requireNonNull(passwordHash);

		this.status = MemberStatus.PENDING;
	}

	public static Member create(
		String email,
		String nickname,
		String password,
		PasswordEncoder passwordEncoder
	) {
		return new Member(email, nickname, passwordEncoder.encode(password));
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
		this.nickname = newNickname;
	}

	public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode(newPassword);
	}
}
