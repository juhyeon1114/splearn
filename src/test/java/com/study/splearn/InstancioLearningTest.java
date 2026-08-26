package com.study.splearn;

import static org.assertj.core.api.Assertions.*;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

public class InstancioLearningTest {

	enum UserStatus {PENDING, ACTIVE, INACTIVE}

	@Getter
	@ToString
	class User {
		Long id;
		String name;
		String email;
		UserStatus status;
	}

	record UserRegisterRequest(
		@Email String email,
		@Size(min = 5, max = 6) String nickname,
		@Size(min = 8, max = 10) String password
	) {
	}

	@Test
	@DisplayName("기본 사용")
	void asfd() {
		var user = Instancio.of(User.class)
			.ignore(Select.field(User::getId)) // ignore: 랜덤 생성 무시
			.generate(Select.field(User::getEmail), gen -> gen.net().email()) // generate: Instancio에서 제공하는 Generate 로직으로 값 생성
			.supply(Select.field(User::getName), () -> "John Doe") // supply: 커스텀 로직으로 값 생성
			.set(Select.field(User::getStatus), UserStatus.PENDING) // set: 특정 값 고정
			.create();

		assertThat(user).isNotNull();
		assertThat(user.getId()).isNull();
		assertThat(user.getEmail()).contains("@");
		assertThat(user.getName()).isEqualTo("John Doe");
		assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
	}

	@Test
	@DisplayName("모델 사용")
	void adfs() {
		var model = Instancio.of(User.class)
			.ignore(Select.field(User::getId))
			.generate(Select.field(User::getEmail), gen -> gen.net().email())
			.supply(Select.field(User::getName), () -> "John Doe")
			.set(Select.field(User::getStatus), UserStatus.PENDING)
			.toModel();

		for (int i = 0; i < 100; i++) {
			var user = Instancio.of(model).create();
			System.out.println(user);

			assertThat(user).isNotNull();
			assertThat(user.getId()).isNull();
			assertThat(user.getEmail()).contains("@");
			assertThat(user.getName()).isEqualTo("John Doe");
			assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
		}
	}

	@Test
	@DisplayName("어노테이션 사용")
	void afsdafd() {
		for (int i = 0; i < 100; i++) {
			var userRegisterRequest = Instancio.of(UserRegisterRequest.class).create();

			System.out.println(userRegisterRequest);

			assertThat(userRegisterRequest.email()).contains("@");
			assertThat(userRegisterRequest.nickname()).hasSizeBetween(5, 6);
			assertThat(userRegisterRequest.password()).hasSizeBetween(8, 10);
		}
	}

}
