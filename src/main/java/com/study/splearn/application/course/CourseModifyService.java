package com.study.splearn.application.course;

import com.study.splearn.application.course.provided.CourseCreateRequest;
import com.study.splearn.application.course.provided.CourseCreator;
import com.study.splearn.application.course.provided.CourseFinder;
import com.study.splearn.application.course.provided.CourseInfoUpdateRequest;
import com.study.splearn.application.course.provided.CourseValidator;
import com.study.splearn.application.course.required.CourseRepository;
import com.study.splearn.application.instructor.privided.InstructorFinder;
import com.study.splearn.domain.course.Course;
import com.study.splearn.support.stereotype.ValidatedApplicationService;
import com.study.splearn.support.stereotype.ValidationException;

import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator {
	private final CourseRepository courseRepository;
	private final CourseFinder courseFinder;
	private final CourseValidator courseValidator;
	private final InstructorFinder instructorFinder;

	@Override
	public Course create(CourseCreateRequest createRequest) throws ValidationException {
		var instructor = instructorFinder.find(createRequest.instructorId());

		courseValidator.validateForCreate(instructor, createRequest);

		return courseRepository.save(new Course(instructor, createRequest.title(), createRequest.description()));
	}

	@Override
	public Course updateInfo(Long courseId, CourseInfoUpdateRequest updateRequest) throws ValidationException {
		var course = courseFinder.find(courseId);

		courseValidator.validateForUpdate(course, updateRequest);

		course.updateInfo(updateRequest.toInfo());

		return courseRepository.save(course);
	}
}
