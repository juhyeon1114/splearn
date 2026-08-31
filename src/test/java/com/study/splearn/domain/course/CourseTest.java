package com.study.splearn.domain.course;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.splearn.domain.instructor.InstructorFixture;

class CourseTest {
	@Test
	@DisplayName("create")
	void adsfa() {
		var instructor = InstructorFixture.createApprovedInstructor();

		var course = new Course(instructor, "title", "desc");

		assertThat(course).isNotNull();
		assertThat(course.getTitle()).isEqualTo("title");
		assertThat(course.getStatus()).isEqualTo(CourseStatus.DRAFT);
		assertThat(course.getInstructor()).isEqualTo(instructor);
		assertThat(course.getDetail().getCreatedAt()).isNotNull();
	}

	@Test
	@DisplayName("비활성 Instructor는 강의를 만들 수 없다.")
	void sadf() {
		var instructor = InstructorFixture.createPendingInstructor();

		Assertions.assertThatThrownBy(() -> new Course(instructor, "title", "desc"))
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	@DisplayName("submitForReview")
	void adfs() {
		var course = CourseFixture.createCourse();

		course.submitForReview();

		assertThat(course.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
	}

	@Test
	@DisplayName("publish")
	void ad() {
		var course = CourseFixture.createCourse();
		course.submitForReview();

		course.publish();

		assertThat(course.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
		assertThat(course.getDetail().getPublishedAt()).isNotNull();
	}

	@Test
	@DisplayName("")
	void afdssadfasdf() {
		var course = CourseFixture.createCourse();
		course.submitForReview();
		course.publish();

		course.archive();

		assertThat(course.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
		assertThat(course.getDetail().getPublishedAt()).isNotNull();
		assertThat(course.getDetail().getArchivedAt()).isNotNull();
	}

}
