package com.study.splearn.application.member.required;

import static com.study.splearn.domain.member.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.study.splearn.domain.member.Member;

import jakarta.persistence.EntityManager;

@DataJpaTest
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	@DisplayName("멤버 등록")
	void test123() {
		var member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());

		assertThat(member.getId()).isNull();

		memberRepository.save(member);

		assertThat(member.getId()).isNotNull();

		entityManager.flush();
	}

	@Test
	@DisplayName("이메일 중복 시 등록 실패")
	void test12() {
		var member1 = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
		memberRepository.save(member1);
		entityManager.flush();

		var member2 = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());

		assertThatThrownBy(() -> {
			memberRepository.save(member2);
			entityManager.flush();
		}).isInstanceOf(DataIntegrityViolationException.class);
	}

}
