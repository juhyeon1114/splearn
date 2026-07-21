package com.study.splearn.application.required;

import static com.study.splearn.domain.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.study.splearn.domain.Member;

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
		var member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

		assertThat(member.getId()).isNull();

		memberRepository.save(member);

		assertThat(member.getId()).isNotNull();

		entityManager.flush();
	}

}
