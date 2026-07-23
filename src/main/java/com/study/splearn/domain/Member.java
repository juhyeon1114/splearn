package com.study.splearn.domain;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NaturalId
	@Embedded
	private Email email;

	private String nickname;

	private String passwordHash;

	@Enumerated(EnumType.STRING)
	private MemberStatus status;

	public static Member register(
		MemberRegisterRequest memberRegisterRequest,
		PasswordEncoder passwordEncoder
	) {
		var member = new Member();

		member.email = new Email(requireNonNull(memberRegisterRequest.email()));
		member.nickname = requireNonNull(memberRegisterRequest.nickname());
		member.passwordHash = passwordEncoder.encode(requireNonNull(memberRegisterRequest.password()));

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
