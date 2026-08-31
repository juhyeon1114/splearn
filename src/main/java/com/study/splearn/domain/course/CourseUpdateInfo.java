package com.study.splearn.domain.course;

import org.jspecify.annotations.Nullable;

import jakarta.validation.constraints.Size;

public record CourseUpdateInfo(
	@Size(min = 2, max = 100) String title,
	@Nullable String description
) {
}
