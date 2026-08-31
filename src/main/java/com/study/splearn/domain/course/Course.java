package com.study.splearn.domain.course;

import static java.util.Objects.*;

import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

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
	Instructor instructor;

	@Column(nullable = false, length = 100)
	String title;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	CourseStatus status;

	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	CourseDetail detail;

	public Course(Instructor instructor, String title, @Nullable String description) {
		instructor.ensureActive();

		this.instructor = requireNonNull(instructor);
		this.title = requireNonNull(title);
		this.status = CourseStatus.DRAFT;

		this.detail = new CourseDetail(description);
	}

	public void submitForReview() {
		Assert.state(this.status == CourseStatus.DRAFT, "DRAFT 상태가 아님");

		this.status = CourseStatus.IN_REVIEW;
	}

	public void publish() {
		Assert.state(this.status == CourseStatus.IN_REVIEW, "IN_REVIEW 상태가 아님");

		this.status = CourseStatus.PUBLISHED;
		this.detail.publish();
	}

	public void archive() {
		Assert.state(this.status == CourseStatus.PUBLISHED, "PUBLISHED 상태가 아님");

		this.status = CourseStatus.ARCHIVED;
		this.detail.archive();
	}

	public boolean isPublished() {
		return this.status == CourseStatus.PUBLISHED;
	}

	public void ensurePublished() {
		Assert.state(this.isPublished(), "PUBLISHED 상태가 아님");
	}
}
