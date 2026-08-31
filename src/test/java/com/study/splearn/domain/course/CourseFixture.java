package com.study.splearn.domain.course;

import static org.instancio.Select.*;

import java.time.LocalDateTime;

import org.instancio.Instancio;

import com.study.splearn.domain.instructor.InstructorFixture;

public class CourseFixture {

	public static Course createCourse() {
		var instructor = InstructorFixture.createApprovedInstructor();

		var courseDetail = Instancio.of(CourseDetail.class)
			.generate(field(CourseDetail::getDescription), gen -> gen.string().maxLength(500).nullable())
			.set(field(CourseDetail::getCreatedAt), LocalDateTime.now())
			.create();

		return Instancio.of(Course.class)
			.ignore(field(Course::getId))
			.generate(field(Course::getTitle), gen -> gen.string().minLength(2).maxLength(100))
			.set(field(Course::getStatus), CourseStatus.DRAFT)
			.set(field(Course::getInstructor), instructor)
			.set(field(Course::getDetail), courseDetail)
			.create();
	}

}
