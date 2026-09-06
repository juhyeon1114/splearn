package com.study.splearn.application.course.required;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.study.splearn.application.instructor.required.InstructorRepository;
import com.study.splearn.application.member.required.MemberRepository;
import com.study.splearn.domain.AbstractEntity;
import com.study.splearn.domain.course.CourseFixture;
import com.study.splearn.domain.instructor.Instructor;
import com.study.splearn.domain.instructor.InstructorFixture;
import com.study.splearn.domain.member.Member;
import com.study.splearn.domain.member.MemberFixture;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@DataJpaTest
@RequiredArgsConstructor
class CourseRepositoryTest {
	final CourseRepository courseRepository;
	final EntityManager entityManager;
	final MemberRepository memberRepository;
	final InstructorRepository instructorRepository;

	Member member;
	Member member2;
	Instructor instructor;
	Instructor instructor2;

	@BeforeEach
	void setUp() {
		member = memberRepository.save(MemberFixture.createActiveMember());
		instructor = instructorRepository.save(InstructorFixture.createApprovedInstructor(member));
		member2 = memberRepository.save(MemberFixture.createActiveMember());
		instructor2 = instructorRepository.save(InstructorFixture.createApprovedInstructor(member2));
	}

	@Test
	void saveAndFind() {
		var course = CourseFixture.createCourse(instructor);
		course = courseRepository.save(course);

		assertThat((course)).isNotNull();

		entityManager.flush();
		entityManager.clear();

		var found = courseRepository.findById(course.getId()).orElseThrow();

		assertThat(found).isEqualTo(course);
		assertThat(found.getId()).isEqualTo(course.getId());
		assertThat(found.getInstructor()).isEqualTo(instructor);
		assertThat(found.getInstructor().getMember()).isEqualTo(member);
	}

	@Test
	void findByTitleContaining() {
		var ids = Stream.of(
				CourseFixture.createCourse(instructor, "Hello Spring 1"),
				CourseFixture.createCourse(instructor, "Hello Spring 2"),
				CourseFixture.createCourse(instructor, "Hello Spring 10"),
				CourseFixture.createCourse(instructor, "Hello Spring 20")
			).map(courseRepository::save)
			.map(AbstractEntity::getId)
			.toList();

		var allResults = courseRepository.findByTitleContaining("Hello Spring");
		assertThat(allResults).size().isEqualTo(4);
		assertThat(allResults).extracting(AbstractEntity::getId).containsExactlyInAnyOrderElementsOf(ids);

		var filteredResults = courseRepository.findByTitleContaining("Hello Spring 1");
		assertThat(filteredResults).size().isEqualTo(2);
		assertThat(filteredResults).extracting(AbstractEntity::getId).containsExactlyInAnyOrder(ids.get(0), ids.get(2));
	}

	@Test
	void findByInstructor() {
		var course1 = CourseFixture.createCourse(instructor);
		course1 = courseRepository.save(course1);

		var course2 = CourseFixture.createCourse(instructor2);
		course2 = courseRepository.save(course2);

		var found = courseRepository.findByInstructor(instructor);
		assertThat(found).size().isEqualTo(1);
		assertThat(found).extracting(AbstractEntity::getId).containsExactlyInAnyOrder(course1.getId());

		found = courseRepository.findByInstructor(instructor2);
		assertThat(found).size().isEqualTo(1);
		assertThat(found).extracting(AbstractEntity::getId).containsExactlyInAnyOrder(course2.getId());
	}

	@Test
	void duplicateTitleOfSameInstructor() {
		courseRepository.save(CourseFixture.createCourse(instructor, "Hello Spring"));
		entityManager.flush();

		assertThatThrownBy(() -> {
			courseRepository.save(CourseFixture.createCourse(instructor, "Hello Spring"));
			entityManager.flush();
		}).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	void duplicateTitleOfDifferentInstructor() {
		courseRepository.save(CourseFixture.createCourse(instructor, "Hello Spring"));
		courseRepository.save(CourseFixture.createCourse(instructor2, "Hello Spring"));
		entityManager.flush();

		assertThat(courseRepository.findByTitleContaining("Hello Spring")).hasSize(2);
	}

}
