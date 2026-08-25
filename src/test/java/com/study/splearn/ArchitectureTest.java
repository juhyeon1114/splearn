package com.study.splearn;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.*;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import jakarta.persistence.Entity;

/**
 * 도메인, 애플리케이션, 어댑터 세 계층의 경계를 검증한다.
 * 의존은 어댑터에서 애플리케이션으로, 애플리케이션에서 도메인으로만 흐른다.
 */
@AnalyzeClasses(packagesOf = SplearnApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

	private static final String DOMAIN = "com.study.splearn.domain..";
	private static final String APPLICATION = "com.study.splearn.application..";
	private static final String ADAPTER = "com.study.splearn.adapter..";

	@ArchTest
	static final ArchRule 계층_의존_방향 = layeredArchitecture()
		.consideringOnlyDependenciesInLayers()
		.layer("도메인").definedBy(DOMAIN)
		.layer("애플리케이션").definedBy(APPLICATION)
		.layer("어댑터").definedBy(ADAPTER)
		.whereLayer("어댑터").mayNotBeAccessedByAnyLayer()
		.whereLayer("애플리케이션").mayOnlyBeAccessedByLayers("어댑터")
		.whereLayer("도메인").mayOnlyBeAccessedByLayers("애플리케이션", "어댑터");

	@ArchTest
	static final ArchRule 패키지_순환_참조_없음 = slices()
		.matching("com.study.splearn.(*)..")
		.should().beFreeOfCycles();

	@ArchTest
	static final ArchRule 어댑터끼리_서로_의존하지_않음 = slices()
		.matching("com.study.splearn.adapter.(*)..")
		.should().notDependOnEachOther();

	@ArchTest
	static final ArchRule 웹_기술은_어댑터_안에만 = noClasses()
		.that().resideInAnyPackage(DOMAIN, APPLICATION)
		.should().dependOnClassesThat().resideInAnyPackage("org.springframework.web..", "jakarta.servlet..")
		.as("도메인과 애플리케이션은 웹 기술에 의존하지 않는다");

	@ArchTest
	static final ArchRule 엔티티는_도메인에만 = classes()
		.that().areAnnotatedWith(Entity.class)
		.should().resideInAPackage(DOMAIN)
		.as("@Entity 클래스는 도메인 패키지에 있어야 한다");

	@ArchTest
	static final ArchRule 서비스는_애플리케이션에만 = classes()
		.that().areMetaAnnotatedWith(Service.class)
		.and().areNotAnnotations()
		.should().resideInAPackage(APPLICATION)
		.as("@Service 클래스는 애플리케이션 패키지에 있어야 한다");

	@ArchTest
	static final ArchRule 컨트롤러는_어댑터에만 = classes()
		.that().areMetaAnnotatedWith(RestController.class).or().areMetaAnnotatedWith(ControllerAdvice.class)
		.and().areNotAnnotations()
		.should().resideInAPackage(ADAPTER)
		.as("컨트롤러는 어댑터 패키지에 있어야 한다");

	@ArchTest
	static final ArchRule 어댑터_구현체는_스프링_빈 = classes()
		.that().resideInAnyPackage("com.study.splearn.adapter.email..", "com.study.splearn.adapter.security..")
		.should().beAnnotatedWith(Component.class)
		.as("외부 연동 어댑터는 스프링 빈으로 등록한다");

}
