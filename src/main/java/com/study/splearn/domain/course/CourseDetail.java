package com.study.splearn.domain.course;

import java.time.LocalDateTime;

import com.study.splearn.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CourseDetail extends AbstractEntity {
	@Column(length = 500)
	String description;

	LocalDateTime createdAt;

	LocalDateTime publishedAt;

	LocalDateTime archivedAt;

	public CourseDetail(String description) {
		this.description = description;
		this.createdAt = LocalDateTime.now();
	}

	public void publish() {
		this.publishedAt = LocalDateTime.now();
	}

	public void archive() {
		this.archivedAt = LocalDateTime.now();
	}
}
