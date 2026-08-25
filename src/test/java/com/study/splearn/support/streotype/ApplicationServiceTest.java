package com.study.splearn.support.streotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.study.splearn.SplearnTestConfiguration;

@SpringBootTest
@Transactional // 개별 테스트 종료 후, 트랜잭션 롤백
@Import(SplearnTestConfiguration.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationServiceTest {
}
