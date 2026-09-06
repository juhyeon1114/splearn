package com.study.splearn.application.course.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.study.splearn.application.course.required.CourseRepository;
import com.study.splearn.domain.course.CourseFixture;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.support.stereotype.ValidationException;
import com.study.splearn.support.streotype.ApplicationServiceTest;
import com.study.splearn.support.test.BaseApplicationServiceTest;

import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseValidatorTest extends BaseApplicationServiceTest {
	final CourseValidator courseValidator;
	final CourseRepository courseRepository;

	Instructor instructor1;
	Instructor instructor2;

	@BeforeEach
	void setUp() {
		this.instructor1 = prepareInstructor();
		this.instructor2 = prepareInstructor();
	}

	@Test
	void titleDuplicationForCreate() {
		var course1 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean spring"));
		var course2 = courseRepository.save(CourseFixture.createCourse(instructor2, "Clean Code"));

		// OK
		courseValidator.validateForCreate(instructor1, new CourseCreateRequest(instructor1.getId(), "Spring 7", null));

		// 중복 제목
		assertThatThrownBy(
			() -> courseValidator.validateForCreate(instructor1, new CourseCreateRequest(instructor1.getId(), "Clean spring", null)))
			.isInstanceOfSatisfying(
				ValidationException.class, e -> {
					assertThat(e.getErrors().size()).isEqualTo(1);
				}
			);

		// Instructor가 다른 경우에는 제목이 중복되도 됨
		courseValidator.validateForCreate(instructor2, new CourseCreateRequest(instructor2.getId(), "Clean spring", null));
	}

	@Test
	void testDuplicationForUpdate() {
		var course1_1 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean spring"));
		var course1_2 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Code"));
		var course2_1 = courseRepository.save(CourseFixture.createCourse(instructor2, "Clean spring"));

		// OK
		courseValidator.validateForUpdate(course1_1, CourseFixture.createCourseUpdateRequest(course1_1.getTitle()));

		// 중복 발생
		assertThatThrownBy(
			() -> courseValidator.validateForUpdate(course1_1, CourseFixture.createCourseUpdateRequest(course1_2.getTitle())))
			.isInstanceOfSatisfying(
				ValidationException.class, e -> {
					assertThat(e.getErrors().size()).isEqualTo(1);
				}
			);
	}

}
