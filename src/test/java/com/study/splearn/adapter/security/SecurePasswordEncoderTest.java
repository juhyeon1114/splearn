package com.study.splearn.adapter.security;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SecurePasswordEncoderTest {

	@Test
	@DisplayName("패스워드 인코더")
	void test123() {
		var encoder = new SecurePasswordEncoder();

		var passwordHash = encoder.encode("password");

		assertThat(encoder.matches("password", passwordHash)).isTrue();
		assertThat(encoder.matches("wrong", passwordHash)).isFalse();
	}

}
