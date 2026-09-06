package com.study.splearn.application.course.provided;

import static com.study.splearn.domain.course.CourseStatus.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.study.splearn.domain.course.Course;
import com.study.splearn.support.stereotype.ApplicationService;
import com.study.splearn.support.test.BaseApplicationServiceTest;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
class CoursePublisherTest extends BaseApplicationServiceTest {
	final CoursePublisher coursePublisher;

	Course course;

	@BeforeEach
	void setUp() {
		course = prepareCourse();
	}

	@Test
	void submitForReview() {
		var courseForReview = coursePublisher.submitForReview(course.getId());

		assertThat(courseForReview.getStatus()).isEqualTo(IN_REVIEW);
	}

	@Test
	void publish() {
		coursePublisher.submitForReview(course.getId());

		var courseForPublish = coursePublisher.publish(course.getId());

		assertThat(courseForPublish.getStatus()).isEqualTo(PUBLISHED);
	}

	@Test
	void archive() {
		coursePublisher.submitForReview(course.getId());
		coursePublisher.publish(course.getId());

		var courseForArchive = coursePublisher.archive(course.getId());

		assertThat(courseForArchive.getStatus()).isEqualTo(ARCHIVED);
	}

}
