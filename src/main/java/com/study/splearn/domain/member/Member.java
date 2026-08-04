package com.study.splearn.domain.member;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import java.util.Objects;

import org.hibernate.annotations.NaturalId;

import com.study.splearn.domain.AbstractEntity;
import com.study.splearn.domain.shared.Email;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@ToString(callSuper = true, exclude = "detail")
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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

		member.detail = MemberDetail.create();

		return member;
	}

	public void activate() {
		state(this.status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

		this.status = MemberStatus.ACTIVE;
		this.detail.activate();
	}

	public void deactivate() {
		state(this.status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

		this.status = MemberStatus.DEACTIVATED;
		this.detail.deactivate();
	}

	public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, this.passwordHash);
	}

	public void changeNickname(String newNickname) {
		this.nickname = requireNonNull(newNickname);
	}

	public void updateInfo(MemberInfoUpdateRequest updateRequest) {
		state(getStatus() == MemberStatus.ACTIVE, "등록 완료 상태가 아니면 정보를 수정할 수 없습니다");

		this.nickname = Objects.requireNonNull(updateRequest.nickname());

		this.detail.updateInfo(updateRequest);
	}

	public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode((requireNonNull(newPassword)));
	}

	public boolean isActive() {
		return this.status == MemberStatus.ACTIVE;
	}
}
