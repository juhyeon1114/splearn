package com.study.splearn.application.course.provided;

import com.study.splearn.domain.course.Course;

import jakarta.validation.Valid;

public interface CourseCreator {
	Course create(@Valid CourseCreateRequest createRequest);

	Course updateInfo(Long courseId, @Valid CourseInfoUpdateRequest updateRequest);
}
