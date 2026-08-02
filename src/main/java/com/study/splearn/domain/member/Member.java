package com.study.splearn.domain.member;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import org.hibernate.annotations.NaturalId;

import com.study.splearn.domain.AbstractEntity;
import com.study.splearn.domain.shared.Email;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(
	name = "member",
	uniqueConstraints = @UniqueConstraint(name = "uk_member_email_address", columnNames = "email_address")
)
@ToString(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

	@NaturalId
	@Embedded
	private Email email;

	@Column(length = 100, nullable = false)
	private String nickname;

	@Column(length = 200, nullable = false)
	private String passwordHash;

	@Column(length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberStatus status;

	@OneToOne
	private MemberDetail detail;

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
