package com.study.splearn.domain.instructor;

import org.springframework.util.Assert;

import com.study.splearn.domain.AbstractEntity;
import com.study.splearn.domain.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "instructor")
@Entity
@Getter
@ToString(callSuper = true, exclude = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends AbstractEntity {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Member member;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private InstructorStatus status;

	public static Instructor apply(Member member) {
		Assert.state(member.isActive(), "등록 완료 상태가 아닌 회원은 강사 신청을 할 수 없습니다.");

		var instructor = new Instructor();
		instructor.member = member;
		instructor.status = InstructorStatus.PENDING;

		return instructor;
	}

	public void approve() {
		Assert.state(status.equals(InstructorStatus.PENDING), "강사 신청 대기 상태가 아닙니다.");

		this.status = InstructorStatus.ACTIVE;
	}

	public void reject() {
		Assert.state(status.equals(InstructorStatus.PENDING), "강사 신청 대기 상태가 아닙니다.");

		this.status = InstructorStatus.REJECTED;
	}

	public boolean isActive() {
		return status.equals(InstructorStatus.ACTIVE);
	}

	public void ensureActive() {
		Assert.state(isActive(), "강사 상태가 아닙니다.");
	}
}
