package com.study.splearn.domain.course;

import static org.instancio.Instancio.*;
import static org.instancio.Select.*;

import java.time.LocalDateTime;

import org.instancio.Instancio;
import org.jspecify.annotations.Nullable;

import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.instructor.InstructorFixture;

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

}
