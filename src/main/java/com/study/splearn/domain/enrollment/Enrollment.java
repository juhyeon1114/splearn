package com.study.splearn.domain.enrollment;

import static org.springframework.util.Assert.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.NaturalId;

import com.study.splearn.domain.AbstractEntity;
import com.study.splearn.domain.course.Course;
import com.study.splearn.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"member", "course"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_ENROLLMENT_MEMBER_COURSE", columnNames = {"member_id", "course_id"}))
public class Enrollment extends AbstractEntity {
	@NaturalId
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Member member;

	@NaturalId
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Course course;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private EnrollmentStatus status;

	@Column(nullable = false)
	private LocalDateTime enrolledAt;

	private LocalDateTime completedAt;

	public static Enrollment enroll(Member member, Course course) {
		member.ensureActive();
		course.ensurePublished();

		var enrollment = new Enrollment();

		enrollment.member = member;
		enrollment.course = course;
		enrollment.status = EnrollmentStatus.ENROLLED;
		enrollment.enrolledAt = LocalDateTime.now();

		return enrollment;
	}

	public void startStudying() {
		state(this.status == EnrollmentStatus.ENROLLED, "수강 상태가 ENROLLED가 아닙니다.");

		this.status = EnrollmentStatus.STUDYING;
	}

	public void complete() {
		state(this.status == EnrollmentStatus.STUDYING, "수강 상태가 STUDYING이 아닙니다.");

		this.status = EnrollmentStatus.COMPLETED;
		this.completedAt = LocalDateTime.now();
	}
}
