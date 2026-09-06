package com.study.splearn.application.course.provided;

import com.study.splearn.domain.course.Course;
import com.study.splearn.support.stereotype.ValidationException;

import jakarta.validation.Valid;

public interface CourseCreator {
	Course create(@Valid CourseCreateRequest createRequest) throws ValidationException;

	Course updateInfo(Long courseId, @Valid CourseInfoUpdateRequest updateRequest);
}
