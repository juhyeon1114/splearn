package com.study.splearn.support.test;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import com.study.splearn.application.course.provided.CourseCreator;
import com.study.splearn.application.course.provided.CoursePublisher;
import com.study.splearn.application.instructor.privided.InstructorApplication;
import com.study.splearn.application.member.provided.MemberRegister;
import com.study.splearn.domain.course.Course;
import com.study.splearn.domain.course.CourseFixture;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.instructor.InstructorFixture;
import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.MemberFixture;
import com.study.splearn.support.streotype.ApplicationServiceTest;

@ApplicationServiceTest
public class BaseApplicationServiceTest {
	@Autowired
	MemberRegister memberRegister;
	@Autowired
	InstructorApplication instructorApplication;
	@Autowired
	CoursePublisher coursePublisher;
	@Autowired
	CourseCreator courseCreator;

	protected Member member;
	protected Instructor instructor;
	protected Course course;

	protected Instructor prepareInstructor() {
		this.member = prepareMember();

		this.instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
		this.instructor.approve();
		return this.instructor;
	}

	protected @NonNull Member prepareMember() {
		this.member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		this.member.activate();
		return this.member;
	}

	protected Course prepareCourse() {
		prepareInstructor();

		this.course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor, null));
		this.course.updateInfo(CourseFixture.createCourseUpdateRequest(null).toInfo());

		return this.course;
	}
}
