package com.study.splearn.support.test;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.study.splearn.application.course.required.CourseRepository;
import com.study.splearn.application.enrollment.required.EnrollmentRepository;
import com.study.splearn.application.instructor.required.InstructorRepository;
import com.study.splearn.application.member.required.MemberRepository;
import com.study.splearn.domain.course.Course;
import com.study.splearn.domain.course.CourseFixture;
import com.study.splearn.domain.enrollment.Enrollment;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.instructor.InstructorFixture;
import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.MemberFixture;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class BaseRepositoryTest {
	@Autowired
	protected EntityManager entityManager;

	@Autowired
	protected MemberRepository memberRepository;

	@Autowired
	protected InstructorRepository instructorRepository;

	@Autowired
	protected CourseRepository courseRepository;

	@Autowired
	protected EnrollmentRepository enrollmentRepository;

	protected Member member;
	protected Instructor instructor;
	protected Course course;
	protected Enrollment enrollment;

	protected Enrollment prepareEnrollment(Member member1, Course course) {
		this.enrollment = Enrollment.enroll(member1, course);
		this.enrollment = enrollmentRepository.save(this.enrollment);

		return enrollment;
	}

	protected Course preparePublishedCourse() {
		prepareApprovedInstructor();

		this.course = courseRepository.save(CourseFixture.createCourse(this.instructor));

		this.course.updateInfo(CourseFixture.createCourseUpdateRequest(null).toInfo());
		this.course.submitForReview();
		this.course.publish();

		return this.course;
	}

	protected @NonNull Instructor prepareApprovedInstructor() {
		prepareActiveMember();

		this.instructor = instructorRepository.save(InstructorFixture.createApprovedInstructor(this.member));

		return this.instructor;
	}

	protected Member prepareActiveMember() {
		this.member = memberRepository.save(MemberFixture.createActiveMember());

		return this.member;
	}
}
