package com.study.splearn.application.course.required;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.study.splearn.domain.course.Course;
import com.study.splearn.domain.instructor.Instructor;

public interface CourseRepository extends Repository<Course, Long> {
	Course save(Course course);

	Optional<Course> findById(Long courseId);

	List<Course> findByTitleContaining(String title);

	List<Course> findByInstructorId(Long instructorId);

	default List<Course> findByInstructor(Instructor instructor) {
		return findByInstructorId(instructor.getId());
	}
}
