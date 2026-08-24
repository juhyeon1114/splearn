package com.study.splearn.application.instructor.required;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.splearn.domain.instructor.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

	Optional<Instructor> findByMember_Id(Long memberId);

}
