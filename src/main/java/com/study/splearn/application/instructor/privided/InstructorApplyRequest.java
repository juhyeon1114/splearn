package com.study.splearn.application.instructor.privided;

import jakarta.validation.constraints.NotNull;

public record InstructorApplyRequest(
	@NotNull Long memberId
) {
}
