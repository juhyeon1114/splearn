package com.study.splearn.adapter.webapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.study.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.study.splearn.application.member.provided.MemberRegister;
import com.study.splearn.application.member.provided.MemberRegisterRequest;
import com.study.splearn.support.stereotype.WebApiAdapter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@WebApiAdapter
@RequiredArgsConstructor
public class MemberApi {

	private final MemberRegister memberRegister;

	@PostMapping("/api/members")
	public MemberRegisterResponse register(
		@RequestBody @Valid MemberRegisterRequest registerRequest
	) {
		var member = memberRegister.register(registerRequest);

		return MemberRegisterResponse.of(member);
	}

}
