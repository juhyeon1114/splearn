package com.study.splearn.application.course;

import java.util.List;

import com.study.splearn.application.course.provided.CourseFinder;
import com.study.splearn.application.course.required.CourseRepository;
import com.study.splearn.domain.course.Course;
import com.study.splearn.support.stereotype.ApplicationService;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CourseQueryService implements CourseFinder {
	private final CourseRepository courseRepository;

	@Override
	public Course find(Long courseId) {
		return courseRepository.findById(courseId)
			.orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 업습니다. ID: " + courseId));
	}

	@Override
	public List<Course> findByKeyword(String title) {
		return courseRepository.findByTitleContaining(title);
	}

	@Override
	public List<Course> findByInstructor(Long instructorId) {
		return courseRepository.findByInstructorId(instructorId);
	}
}
