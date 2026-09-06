package com.study.splearn.application.course.provided;

import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.support.stereotype.ValidationException;

public interface CourseValidator {
	void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException;
}
