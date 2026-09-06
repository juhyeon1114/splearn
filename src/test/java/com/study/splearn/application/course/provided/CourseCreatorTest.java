package com.study.splearn.application.course.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.study.splearn.domain.course.CourseFixture;
import com.study.splearn.support.streotype.ApplicationServiceTest;
import com.study.splearn.support.test.BaseApplicationServiceTest;

import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseCreatorTest extends BaseApplicationServiceTest {
	final CourseCreator courseCreator;

	@Test
	void create() {
		var instructor1 = prepareInstructor();

		var course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor1));

		assertThat(course).isNotNull();
		assertThat(course.getId()).isNotNull();
	}

	@Test
	void updateInfo() {
		var instructor1 = prepareInstructor();

		var course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor1));

		var updateRequest = CourseFixture.createCourseUpdateRequest("UPDATE");
		var updated = courseCreator.updateInfo(course.getId(), updateRequest);

		assertThat(updated.getTitle()).isEqualTo(updateRequest.title());
		assertThat(updated.getDetail().getDescription()).isEqualTo(updateRequest.description());
	}

}
