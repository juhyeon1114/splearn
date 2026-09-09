package com.study.splearn.domain.course;

import static org.instancio.Instancio.*;
import static org.instancio.Select.*;

import java.time.LocalDateTime;

import org.instancio.Instancio;
import org.jspecify.annotations.Nullable;

import com.study.splearn.application.course.provided.CourseCreateRequest;
import com.study.splearn.application.course.provided.CourseInfoUpdateRequest;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.instructor.InstructorFixture;

import jakarta.validation.Valid;

public class CourseFixture {

	public static Course createCourse(@Nullable Instructor instructor, @Nullable String title) {
		var courseDetail = Instancio.of(CourseDetail.class)
			.ignore(field(CourseDetail::getId))
			.generate(field(CourseDetail::getDescription), gen -> gen.string().maxLength(500))
			.set(field(CourseDetail::getCreatedAt), LocalDateTime.now())
			.create();

		return Instancio.of(Course.class)
			.ignore(field(Course::getId))
			.set(field(Course::getTitle), title == null ? gen().string().minLength(2).maxLength(100).get() : title)
			.set(field(Course::getStatus), CourseStatus.DRAFT)
			.set(field(Course::getInstructor), instructor == null ? InstructorFixture.createApprovedInstructor() : instructor)
			.set(field(Course::getDetail), courseDetail)
			.create();
	}

	public static Course createCourse() {
		return createCourse(null, null);
	}

	public static Course createCourse(Instructor instructor) {
		return createCourse(instructor, null);
	}

	public static @Valid CourseCreateRequest createCourseCreateRequest(Instructor instructor, @Nullable String title) {
		return Instancio.of(CourseCreateRequest.class)
			.set(field(CourseCreateRequest::instructorId), instructor.getId())
			.set(field(CourseCreateRequest::title), title == null ? gen().string().minLength(2).maxLength(100).get() : title)
			.set(field(CourseCreateRequest::description), gen().string().maxLength(500).get())
			.create();
	}

	public static @Valid CourseCreateRequest createCourseCreateRequest(Instructor instructor) {
		return createCourseCreateRequest(instructor, null);
	}

	public static @Valid CourseInfoUpdateRequest createCourseUpdateRequest(@Nullable String title) {
		return Instancio.of(CourseInfoUpdateRequest.class)
			.set(field(CourseInfoUpdateRequest::title), title == null ? gen().string().minLength(2).maxLength(100).get() : title)
			.set(field(CourseInfoUpdateRequest::description), gen().string().maxLength(500).get())
			.create();
	}

	public static Course createPublishedCourse() {
		var course = createCourse();
		course.updateInfo(createCourseUpdateRequest(course.getTitle()).toInfo());
		course.submitForReview();
		course.publish();
		return course;
	}

}
