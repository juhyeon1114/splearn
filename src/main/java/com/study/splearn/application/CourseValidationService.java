package com.study.splearn.application;

import java.util.ArrayList;
import java.util.List;

import com.study.splearn.application.course.provided.CourseCreateRequest;
import com.study.splearn.application.course.provided.CourseValidator;
import com.study.splearn.application.course.required.CourseRepository;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.support.stereotype.ApplicationService;
import com.study.splearn.support.stereotype.ValidationException;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CourseValidationService implements CourseValidator {
	private final CourseRepository courseRepository;

	@Override
	public void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException {
		instructor.ensureActive();

		List<String> errors = new ArrayList<>();

		checkTitleDuplicationForCreate(instructor, createRequest.title(), errors);
		checkBannedWords(createRequest.title(), errors);
		checkBannedWords(createRequest.description(), errors);

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

	private void checkBannedWords(@Size(min = 2, max = 100) String title, List<String> errors) {
		// todo
	}

	private void checkTitleDuplicationForCreate(Instructor instructor, @Size(min = 2, max = 100) String title, List<String> errors) {
		courseRepository.findByInstructorAndTitle(instructor, title)
			.ifPresent(course -> errors.add("이미 사용중인 강의 제목입니다. " + title));
	}
}
