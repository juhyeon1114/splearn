package com.study.splearn.domain.member;

import java.time.LocalDateTime;

import com.study.splearn.domain.AbstractEntity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {

	private String profile;

	private String introduction;

	private LocalDateTime registeredAt;

	private LocalDateTime activatedAt;

	private LocalDateTime deactivatedAt;

}
