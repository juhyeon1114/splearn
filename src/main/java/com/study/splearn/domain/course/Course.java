package com.study.splearn.domain.course;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;

import com.study.splearn.domain.AbstractEntity;
import com.study.splearn.domain.instructor.Instructor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends AbstractEntity {
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Instructor instructor;

	@Column(nullable = false, length = 100)
	private String title;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private CourseStatus status;

	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CourseDetail detail;

	public Course(Instructor instructor, String title, @Nullable String description) {
		instructor.ensureActive();

		this.instructor = requireNonNull(instructor);
		this.title = requireNonNull(title);
		this.status = CourseStatus.DRAFT;

		this.detail = new CourseDetail(description);
	}

	public void submitForReview() {
		state(this.status == CourseStatus.DRAFT, "DRAFT 상태가 아님");
		state(StringUtils.hasText(this.detail.getDescription()), "강의 소개가 등록되어 있지 않음");

		this.status = CourseStatus.IN_REVIEW;
	}

	public void publish() {
		state(this.status == CourseStatus.IN_REVIEW, "IN_REVIEW 상태가 아님");

		this.status = CourseStatus.PUBLISHED;
		this.detail.publish();
	}

	public void archive() {
		state(this.status == CourseStatus.PUBLISHED, "PUBLISHED 상태가 아님");

		this.status = CourseStatus.ARCHIVED;
		this.detail.archive();
	}

	public boolean isPublished() {
		return this.status == CourseStatus.PUBLISHED;
	}

	public void ensurePublished() {
		state(this.isPublished(), "PUBLISHED 상태가 아님");
	}

	public void updateInfo(CourseUpdateInfo updateInfo) {
		this.title = requireNonNull(updateInfo.title());
		this.detail.updateInfo(updateInfo);
	}
}
