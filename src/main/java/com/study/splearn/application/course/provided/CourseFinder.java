package com.study.splearn.application.course.provided;

import java.util.List;

import com.study.splearn.domain.course.Course;

/**
 * 강의 조회와 관련된 작업
 */
public interface CourseFinder {
	Course find(Long courseId);

	List<Course> findByKeyword(String title);

	List<Course> findByInstructor(Long instructorId);
}
