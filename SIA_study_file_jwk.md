[TOC]

# Spring in action 2/E

## 1장 스프링 속으로
* 다룰내용
	* 스프링의 빈 컨테이너
	* 스프링의 코어 모듈 살펴보기
	* 더 훌륭해진 스프링 에코시스템
	* 스프링의 새로워진 점
***

#### 1.1 자바 개발 간소화
* Expert One-on-One: J2EE Design and Development
	* 스프링 소개 - Rod Johnson
* 엔터프라이즈 애플리케이션 개발의 복잡함을 해소하기 위해 만듦
* 스프링의 목적은 자바 개발을 폭넓게 간소화하는 것
* 자바 복잡도 간소화를 지원하기 위한 스프링의 네가지 전략
	* POJO를 이용한 가볍고 비침투적인 개발
	* DI와 인터페이스 지향을 통한 느슨한 결합도
	* 애스펙트와 공통 규약을 통한 선언적 프로그래밍
	* 애스펙트와 템플릿을 통한 반복적인 코드 제거
***

###### 1.1.1 POJO의 힘
* 침투적 프로그래밍의 예
	* EJB, Struts, WebWorks, Tapestry 및 수많은 자바 명세와 프레임워크의 초기 버전
* 스프링은 API를 이용해 애플리케이션 코드의 분산을 막음
* 스프링에 특화된 인터페이스 구현이나 스프링 자체에 의존성이 높은 클래스 확장을 거의 요구하지 않음
* 클래스에 스프링의 애너테이션이 붙은 경우를 제외하면 POJO
* 불필요한 요구를 하지 않는 스프링
```JAVA
	package com.habuma.spring;
    public class HelloWorldBean {
    	public String sayHello(){
        	return "Hello World";
        }
    }
```
* POJO는 DI를 통해서 힘을 받는다.
***

###### 1.1.2 종속객체 주입
* DI 동작 방법
	* 실제 애플리케이션에서는 두 개 이상의 클래스가 협력하여 비즈니스 로직을 수행
	* 각 객체는 협력하는 객체에 대한 레퍼런스를 얻기 위해 결합도가 높아지고 테스트하기 어려운 코드가 만들어짐
	* P7 코드1.2 소녀를 구하는 기사는 소녀 구출 임무를 받은 자만 떠날 수 있다.
		* DamselRescuingKnight는 생성자 안에서 RescueDamselQuest를 생성 - 강하게 결함됨
		* embarkOnQuest() 메소드를 테스트하기도 어려움 - quest의 메소드 테스트가 불가능
    * 결합도가 높은 코드
    	> 테스트와 재활용이 어려움
    	> 이해하기 어려움
    	> 오류를 하나 수정하면 다른 오류가 발생하는 경향

    *  결합이 없는 코드
    	> 아무것도 할수 없음

    * 결합은 필요하지만 주의해서 관리해야 함 

	* P7 그림1.1
		> 제 3자에 의해서 생성 시점에 종속객체가 부여됨
		> 객체는 종속객체를 생성하거나 얻지 않음

	* P8 코드1.3 부여된 어떤 Quest도 충분히 감당할 만큼 유연한 BraveKnight
		> Quest를 주입받음 - 생성자 주입 (Constructor injection)
		> BraveKnight는 Quest의 특정 구현체에 결합되지 않음 - Quest인터페이스를 받음
		> 느슨한 결합도 - loose coupling
		> mock 객체를 이용해서 테스트가 쉬움 - P8 코드1.4
***

* 기사에게 원정 임무 주입
	* P9 코드 1.5 SlayDragonQuest는 BraveKnight에 주입될 Quest다.
		> Quest 인터페이스를 구현했으며, BraveKnight에 적합함

	* 와이어링
		* 어떻게 BraveKnight에게 SlayDragonQuest를 줄수 있는가?
		* 어떻게 SlqyDragonQuest에게 PrintStream을 줄 수 있는가?
		* 설정파일(P10 코드 1.6)을 통해서 클래스들을 엮음
		* 설정시 스프링 표현언어 사용 가능
		* 설정파일(P11 코드1.7) - 자바 기반 설정
		* 종속된 클래스를 수정하지 않으면서도 종속성 수정이 가능
***

* 실행해 보기
	* application context에서 빈에 관한 정의들을 바탕으로 빈들을 엮음
		* 객체의 생성과 와이어링을 전적으로 책임 짐
	* ClassPathXmlApplicationContext()
		* XML 설정 파일을 사용할때 좋음
		* P12 코드1.8
			> knights.xml 파일을 기반으로 스프링 애플리케이션 컨텍스트를 생성
***

###### 1.1.3 애스펙트 적용
* 애플리케이션 전체에 걸쳐 사용되는 기능을 재사용할 수 있는 컴포넌트에 담는다.
* 소프트웨어 시스템 내부의 관심사들을 서로 분리하는 기술
* 횡단관심사
	* 로깅, 트랜잭션 관리, 보안 등의 시스템 서비스
	* 여러 컴포넌트에 관련되는 경향이 있음
	* 횡단관심사가 여러 컴포넌트에 퍼지게 될때 복잡해짐
		* 코드의 중복 - 변경 시 모두 변경해야 함
		* 코드가 지저분해짐 - 코드 본영의 기능과 관련없는 코드
	* P14 그림1.2
	* P14 그림1.3
		> 애스펙트 - 여러 컴포넌트를 덮는 담요의 개념
		> 추가적인 개념들을 여러 겹의 이불처럼 덮고 있음
		> 핵심기능은 아무런 변화가 없고, 추가적인 기능은 선언적으로 동작함
***

* AOP 실습
	* 음유시인(서비스)이 기사의 출정과 복귀를 노래하는 로깅 시스템
		* P15 코드1.9 - Minstrel 클래스
			> 두개의 메소드를 가진 클래스 

		* P15 코드1.10
			> BraveKnight와 Minstrel을 합침
			> 문제점
			>> 기사의 관심 범위에 음유시인이 있어야 하나?
			>> 음유시인이 없는 기사를 원할 경우 별도의 null 체크 로직을 둬야 하나?

		* P17 코드1.11
			> Minstrel을 애스펙트로 변경 - 스프링 설정 파일에서 주입
			> Minstrel 빈 선언
			> 포인트컷 정의
			> before, after 어드바이스 선언
			> 설명 포인트
			>> Minstrel은 여전히 POJO
			>> BraveKnight는 Minstrel의 존재를 전혀 모름 - 알 필요도 없음
			>> 애스펙트 적용을 위해서는 스프링 빈으로 등록해야 함
			>
			> AspejctJ Pointcut 표현식
			>> http://groovysunday.tistory.com/201
***

###### 1.1.4 템플릿을 이용한 상투적인 코드 제거
* 상투적 코드
	* 간단한 작업을 위해 반복적으로 작성해야 하는 코드
	* DB에서 데이터를 조회하는 JDBC 코드 등
		* P19 코드1.12
		> 몇줄 안되는 직원 조회 코드가 JDBC 형식의 더미에 묻혀 버림
		> 직원 조회를 위해서 수행하는 작업을 극히 일부분이며 대부분 상투적 코드임

* 스프링의 jdbcTemplate은 JDBC에 필요한 모든 형식 없이 DB 작업을 수행할 수 있게 함
	* P20 코드1.13
		> getEmployeeById() 메소드
		> 이전에 보였던 상투적인 코드가 보이지 않음
		> 모든것은 템플릿 내부에서 처리됨
***

#### 1.2 빈을 담는 그릇, 컨테이너
* 스프링 기반 애플리케이션
	* 스프링 컨테이너 안에서 객체가 태어나고, 자라고, 소멸
* 스프링 컨테이너
	* 객체를 생성하고, 엮어주고, 이들의 전체 생명주기를 관리
	* 크게 두 가지로 구분
		* BeanFactory
			* DI에 대한 기본적인 지원을 제공하는 가장 단순한 컨테이너
			* 지나치게 저수준의 기능을 제공
		* ApplicationContext
			* 빈 팩토리를 확장
			* 프로퍼티 파일에 텍스트 메시지를 읽고 애플리케이션 프레임워크 서비스를 제공
***

###### 1.2.1 또 하나의 컨테이너, 애플리케이션 컨텍스트
* 애플리케이션 컨텍스트 종류
	* AnnotationConfigApplicationContext
		* 자바 기반 설정 클래스에서 스프링 애플리케이션 컨텍스트를 로드
	* AnnotationConfigWebApplicationContext
		* 자바 기반 설정 클래스에서 스프링 웹 애플리케이션 컨텍스트를 로드
	* ClassPathXmlApplicationContext
		* 클래스패스에 위차한 XML 파일에서 컨텍스르 정의 내용을 로드
	* FileSystemXmlApplicationContext
		* 파일 경로로 지정된 XML파일에서 컨텍스트 정의 내용을 로드
	* XmlWebApplicationContext
		* 웹 애플리케이션에 포함된 XML 파일에서 컨텍스트 정의 내용을 로드

* 로딩 코드
	* P23 
	* 로딩 후 getBean() 메소드를 호출하여 스프링 컨테이너에서 빈을 조회
***

###### 1.2.2 빈의 일생
* BeanFactory 컨테이너 내에서 빈이 갖는 구동 생명주기
	* P24 그림1.5
	> P24 그림1.5의 각 과정
***

#### 1.3 스프링 현황
* 스프링 프레임워크는
	* 자바 개발을 쉽게 할 수 있는 여러 가지 방법을 제공
	* 프레임워크 자체를 넘어서 웹 서비스, REST, 모바일 그리고 NoSQL 등의 영역으로 확장
###### 1.3.1 스프링 모듈
* 스프링 다운로드 후 lib 폴더에 존재
* 4.0부터는 20개의 서로 다른 모듈이 있으며, 각 모듈마다 세 개의 JAR 파일이 함께 있음
	* 비이너리 클래스 라이브러리, 소스 JAR파일, JavaDOC JAR파일
* 6개의 기능 카테고리로 구분
	* P26 그림1.7
***

* 6개 기능 카테고리
    * 코어 스프링 컨테이너
    	* 빈 팩토리와 애플리케이션 컨텍스트
    	* 이메일, JNDI 액세스, EJB통합 그리고 스케쥴링 등의 엔터프라이즈 서비스 제공
    * 스프링의 AOP 모듈
    	* 애스펙트 개발의 기반
    	* 객체 간의 결합도를 낮추는데 기여
    * 데이터 액세스와 통합
    	* DB관련 코드를 깔끔하고 간단하게 작성 가능
    	* JMS를 이용한 스프링 추상화를 포함
    * 웹과 리모팅
    	* 스프링 MVC 프레임워크가 별도로 제공됨
    	* RMI, Hessian, Burlap, JAX-WS 그리고 HTTP 호출자 등의 리모팅 기능 제공
    	* REST API에 대한 지원 제공
    * 인스트루멘테이션
    	* JVM에 에이전트를 추가하는 기능을 제공
    	* 톰캣용 위빙 에이전트를 제공
    * 테스팅
    	* 스프링 애플리케이션 테스트에 전념하는 모듈을 제공
***

###### 1.3.2 스프링 포트폴리오
* 전체 스프링 포트폴리오에는 다양한 프레임워크와 라이브러리가 있다.
* 스프링 포트폴리오의 주요 내용
	* 스프링 웹 플로
		* 대화형, 흐름 기반 웹 애플리케이션 구축을 지원하기 위한 스프링의 핵심 MVC 프레임워크
	* 스프링 웹 서비스
		* 규약 우선 웹 서비스 모델을 제공
	* 스프링 시큐리티
		* 스프링 AOP를 이용하여 구현됨
	* 스프링 인티그레이션
		* 공통적인 통합 패턴의 구현체를 스프링의 선언적 방식으로 제공
	* 스프링 배치
		* 데이터의 일괄처리를 지원
	* 스프링 데이터
		* 스프링 내 모든 종류의 DB와의 구동을 쉽게 해줌
	* 스프링 소셜
		* 많은 REST API로 스프링 애플리케이션을 연동하는 것을 도와줌
	* 스프링 모바일
		* 모바일 웹 애플리케이션 개발을 지원하는 스프링 MVC의 새로운 확장 기능
	* 안드로이드용 스프링
		* 스프링프레임워크가 제공하는 간소화의 일부를 사용한, 안드로이드 기반 장치용 기본 애플리케이션의 개발을 지원
	* 스프링 부트
		* 스프링 그 자체를 간소화하기 위해 스프링으로 개발
		* 자동설정 기술을 써서 대부분의 스프링 설정을 제거
***

#### 1.4 스프링의 새로운 기능
* 스프링 각 버전의 새로운 특징과 개선점을 알아보자
***

###### 1.4.1 스프링 3.1에서 새로워진 기능
* 여러 가지 유용한 새 기능과 개선
* 설정을 간소화하고 개선하는데 초점을 둠
* 스프링 3.1의 개선 부분
	* 환경 프로파일을 도입 - 설정파일을 프로파일로 분리
	* enable 어노테이션 추가
	* 선언적 캐싱 지원
	* 새로운 c-네임스페이스 지원 - 생성자 주입 가능
		> http://blog.naver.com/PostView.nhn?blogId=kimnx9006&logNo=220601449510&parentCategoryNo=&categoryNo=29&viewDate=&isShowPopularPosts=false&from=postView
	* 서블릿 3.0을 지원하기 시작
	* JPA 지원 강화 - persistence.xml없에 JPA 설정 가능
	* 스프링MVC에 대한 부분 강화
***

###### 1.4.2 스프링 3.2에서 새로워진 기능
* 주로 스프링 MVC에 초점을 맞춘 릴리즈
* 스프링 3.2의 개선 부분
	* 컨트롤러가 분리된 스레드 내에서 처리
	* 서블릿 컨테이너 없이도 컨트롤러로서의 역할을 수행
	* RestTemplate 기반의 클라이언트 테스트를 지원
	* @ControllerAdvice 추가 - 세가지 메소드를 하나의 클래스에 집합
	* ...
	* @Autowired, @Value 그리고 @Bean 어노테이션은 커스텀 주입과 빈 선언 어노테이션을 생성하기 위한 메타-어노테이션으로 사용
***

###### 1.4.3 스프링 4.0에서 새로워진 기능
* 스프링 4.0의 개선 부분
	* 웹소켓 프로그래밍을 지원
	* 매우 높은 수준의 메시지 기반 모델의 웹 소켓도 지원
	* 새로운 메시지 모듈은 많은 타입을 가지며, 스프링 통합 프로젝트로 넘어옴
	* 자바8의 특징인 람다식을 지원 - 콜백 인터페이스로 작업 가능
	* JSR-310의 데이터 및 시간 API를 포함하는 자바 8 지원
	* Groovy로 개발된 애플리케이션에 대한 지원이 추가되어 쉽게 개발 가능
	* 조건부 빈 생성에 대한 일반적 지원이 추가
	* RestTemplate에 대한 새로운 비동기식 구현이 추가 - 응답을 반환하고 연산 종료 시 콜백 호출 가능
	* 많은 JEE 스펙 지원이 추가
***

#### 1.5 요약
* DI
	* 객체들이 상호 간의 종속관계나 구체적인 구현 방법을 알 필요가 없도록 애플리케이션 객체들을 연결하는 방법
	* 객체간 결합도가 낮아짐
* AOP
	* 애플리케이션에 흩어져 있는 로직을 한 곳으로(애스펙트로) 모을수 있음
	* 각 애스펙트는 런타임에 빈과 연결되어 효과적으로 빈에 새로운 기능을 부여할 수 있음
***

## 2장 빈 와이어링(묶기)	
* 다룰내용
	* 빈(bean) 선언
	* 생성자 주입과 세터 주입
	* 빈 와이어링
	* 빈의 생성과 소멸 제어
* 스프링에서는 컨테이너가 협업할 객체에 대한 레퍼런스를 준다.
* 와이어링
	* 객체 간의 연관관계 형성 작업 - DI 개념의 핵심
* 다음은 스프링 컨테이너를 설정하기 위한 세가지 접근 방법임
***

#### 2.1 스프링 설정 옵션 알아보기
* 어떤 빈을 생성할지, 그들을 어떻게 엮을지 스프링에게 알려주는 것은 개발자의 책임
* 스프링이 제공하는 세가지 와이어링 메커니즘
	* XML에서의 명시적 설정
	* 자바에서의 명시적 설정
	* 내재되어 있는 빈을 찾아 자동으로 와이어링하기
* 가능하다면 자동 설정을 추천 - 명시적인 설정이 적을수록 좋음
* 명시적인 빈 설정을 해야 할때는 JavaConfig를 선호
* 사용하고자하는 XML 네임스페이스의 기능이 JavaConfig에 없을 경우는 XML을 사용
***

#### 2.2 자동으로 빈 와이어링하기
* 스프링의 자동 와이어링 방법 두가지
	* 컴포넌트 스캐닝
		* 애플리케이션 컨텍스트에서 생성되는 빈을 자동으로 발견
	* 오토 와이어링
		* 자동으로 빈 의존성을 충족시킴
* 자동 와이어링의 개념 설명을 위한 예
	* 스테레오 시스템의 몇가지 컴포넌트를 나타내는 빈
	* CompactDisc 클래스 - 스프링이 시작 시에 자동으로 빈으로 생성
	* CDPlayer 클래스 - 스프링이 자동으로 발견해서 여기에 CompactDisc 빈을 주입
***

###### 2.2.1 발견 가능한 빈 만들기
* CD를 정의하는 인터페이스인 CompactDisc
	* P42 코드2.1
	```JAVA
    	package soundsystem;
        
        public interface CompactDisc {
            void play();
        }
    ```
	> 인터페이스로 정의되어 있어 어떤 CD플레이어 구현과 CD 그 자체 사이에 최소한의 커플링을 유지함
***

* CompactDisc를 구현
	* P43 코드2.2
	```JAVA
    	package soundsystem;
        import org.springframework.stereotype.Component;
        
        @Component
        public class SgtPeppers implements CompactDisc {
        	private String title = "Sgt. Pepper's Lonely Hearts Club Band";
            private String artist = "The Beatles";
            
            public void play(){
            	System.out.println("Playing " + title + " by " + artist);
            }
        }
    ```
    > @Component 어노테이션 사용 - 클래스가 컴포넌트 클래스임을 나타내며 빈으로 만들어야 함을 스프링에 알림
***

* 스프링이 어노테이션을 찾아 빈을 등록하도록 처리
	* P43 코드2.3
	```JAVA
    	package soundsystem;
        import org.springframework.context.annotation.ComponentScan;
        import org.springframework.context.annotation.Configuration;
        
        @Configuration
        @ComponentScan
        public class CDPlayerConfig {
        
        }
    ```
    > 자바로 스프링 와이어링 스펙을 정의
    > soundsystem 패키지와 하위 패키지를 스캔하고 어노테이션을 찾아 빈으로 등록한다.

	* P44 코드2.4 XML로 컴포넌트 스캐닝 활성화하기
	```XML
    	<?xml version-"1.0" encoding="UTF-8" ?>
        <beans xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:context="http://www.springframework.org/schema/context"
            xsi:schemaLocation="http://www.springframework.org/schema/beans
            	http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">
            <context:component-scan base-package="soundsystem" />
        </beans>
    ```
***

* 컴포넌트 스캐닝에 의해 발견된 CompactDisc 테스트하기
	* P45 코드2.5
	```JAVA
    	package soundsystem;
        
        import static org.junit.Assert.*;
        
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.test.context.ContextConfiguration;
        import org.springframework.test.context.junit4.SpringJunit4ClassRunner;
        
        @RunWith(SpringJUnit4ClassRunner.class)
        @ContextConfiguration(classes=CDPlayerConfig.class)
        public class CDPlayerTest {
        	@Autowired
            private CompactDisc cd;
            
            @Test
            public void cdShouldNotBeNull(){
            	assertNotNull(cd);
            }
        }
    ```
    > @ContextConfiguration 어노테이션은 CDPlayerConfig 클래스를 통해서 설정을 로드
    > cd 프로퍼티가 null이 아니게 된다 - by autowired
***

###### 2.2.2 컴포넌트 스캔된 빈 명명하기
* 모든 빈은 ID가 주어짐
	* 명시적으로 주어지지 않으면 클래스 명의 첫 글자를 소문자로 바꾼 값을 사용
	* ID 변경은 value로 넣어주면 됨
	```JAVA
    	@Component("lonelyHeartsClub")
        public class SgtPeppers implements CompactDisc {
         ...
        }
    ```
    * @Named 어노테이션으로도 변경가능하지만 선호되지는 않는다.
    ```JAVA
    	package soundsystem;
        import javax.inject.Named;
        
        @Named("loneyHeartsClub")
        public class SgtPeppers implements CompactDisc {
         ...
        }
    ```
***

###### 2.2.3 컴포넌트 스캐닝을 위한 베이스 패키지 세팅
* 베이스 패키지를 명시하는 이유
	* 애플리케이션 코드와 설정 코드의 분리
	* 베이스 패키지 지정 방법
		```JAVA
        	@Configuration
        	@ComponentScan("soundsystem")
            public class CDPlayerConfig {}
        ```
        > 스캔 값 영역에 기술

		```JAVA
        	@Configuration
        	@ComponentScan(basePackages="soundsystem")
            public class CDPlayerConfig {}
        ```
        > basePackages 애트리뷰트를 사용하여 명시
        
        ```JAVA
        	@Configuration
        	@ComponentScan(basePackages={"soundsystem", "video"})
            public class CDPlayerConfig {}
        ```
        > 여러 개의 베이스 패키지 지정

        ```JAVA
        	@Configuration
        	@ComponentScan(basePackageClasses={CDPlayer.class, DVDPlayer.class})
            public class CDPlayerConfig {}
        ```
        > 패키지명의 type safe를 위해 클래스명을 지정
***

###### 2.2.4 오토와이어링되는 빈의 애너테이션
* 오토와이어링
	* 애플리케이션 컨텍스트상에서 다른 빈을 찾아 빈 간의 의존성을 자동으로 만족시키도록 하는 수단
	* @Autowired 어노테이션을 사용
	* 오토와이어링 방법
		* 생성자를 통한 방법
			* P48 코드2.6
				* CompactDisc를 CDPlayer빈으로 주입 by Autowiring
		* 세터 메소드를 통한 방법
			* P49 두번째 코드 예제
	* 생성자나 세터 메소드를 포함한 어떤 메소드든 스프링은 메소드 파라미터에 의존성을 가진다.
		* 한 개의 빈이 일치하면 그 빈은 와이어링 된다.
		* 매칭되는 빈이 없다면 예외가 발생하며, 예외를 피하기 위해서는 required를 false로 해야 함
		```JAVA
        	@Autowired(required=false)
            public CDPlayer(CompactDisc cd){
            	this.cd = cd;
            }
        ```
        > required가 false일때는 코드에서 Null 체크를 해야 함
        > 다중 빈이 의존성을 가질때도 예외를 발생함

	* @Inject
		* @Autowired는 스프링에서 정의한 어노테이션
		* 스프링 기반의 어노테이션 사용이 어려울때 사용 가능
		* 자바 종속 객체 정의에 있음
***

###### 2.2.5 자동 설정 검증하기
* CDPlayer 빈을 통해 콤팩 디스크를 재생하는 CDPlayerTest를 변경
	* P51 예제
***

#### 2.3 자바로 빈 와이어링하기
* 자동설정은 옵션이 아니며 명시적으로 설정해야 한다.
* 명시적 설정 방법
	* 자바
		* 타입세이프하며, 리팩토링이 친화적이므로 명시적 설정을 위해 선호됨
	* XML
***

###### 2.3.1 설정 클래스 만들기
* CDPlayerConfig
```JAVA
	package soundsystem;
    import org.springframework.context.annotation.Configuration;
    
    @Configuration
    public class CDPlayerConfig {
    
    }
```
	> @Configuration으로 어노테이션 해서 설정파일로 식별
	> 명시적인 설정을 위해 @ComponentScan을 제거
***

###### 2.3.2 간단한 빈 선언하기
* 메소드를 만들고 @Bean으로 어노테이션 처리
```JAVA
	@Bean
    public CompactDisc sgtPeppers(){
    	return new SgtPeppers();
    }
```
	> @Bean - 메소드가 빈으로 등록된 객체를 반환해야 함을 나타냄
	> 기본적으로 빈은 @Bean으로 어노테이션된 메소드와 동일한 ID로 지정됨
	> 이름 변경 - @Bean(name="바꿀이름")
***

###### 2.3.3 JavaConfig 주입하기
* JavaConfig에서 빈 와이어링
```JAVA
	@Bean
    public CDPlayer cdPlayer(){
    	return new CDPlayer(sgtPeppers());
    }
```
> 빈 와이어링 - 참조된 빈 메소드를 참조하는 것
> 기본 생성자를 호출해서 생성되는 것이 아니라 스프링이 콜을 중간에 인터셉트해서 메소드에 의해 만들어진 빈은 한번만 만들어진다.

* 설정을 설정클래스, XML 파일, 자동 스캔 등으로 쪼갤 수 있다.
***

#### 2.4 빈을 XML로 와이어링하기
* XML설정이 첫번째 선택지는 아니지만 기존에 많이 사용되고 있으므로 이해가 필요하다.
* 새로 작성하는 스프링은 자동 설정과 JavaConfig를 사용하길 바란다.
***

###### 2.4.1 XML 설정 스펙 만들기
* XML 설정파일 필요
	* P58
***

###### 2.4.2 간단한 빈 선언
* 빈 선언은 &lt;bean&gt; 을 사용
* 기본 생성되는 빈의 이름은 패키지.클래스명#0 다.
* 이름 변경은 id 속성을 사용하면 된다.
* XML 설정은 참조할 자바 타입의 존재유무 체크를 못한다.
***

###### 2.4.3 생성자 주입을 사용하여 빈 초기화하기
* 생성자 주입 방법
	* &lt;constructor-arg&gt; 요소 사용
	```XML
    	<bean id="cdPlayer" class="soundsystem.CDPlayer">
        	<constructor-arg ref="compactDisc" />
        </bean>
    ```
	* 3.0에서 도입된 c-네임스페이스
	```XML
    	<bean id="cdPlayer" class="soundsystem.CDPlayer" c:cd-ref="compactDisc">
    ```
    > c:cd-ref="compactDis" 형태로 사용
    > c: - c-네임스페이스 접두어
    > cd : 생성자 인자명
    > -ref : 빈 레퍼런스 주입
    > compactDisc : 주입용 빈 ID
***

* 와이어링 컬렉션
	* 인자를 전달할때 &lt;list&gt; 사용
		* P65 맨 아래 예
	* Bean 참조는 ref 사용
		* P66 중간 예
	* set 도 사용 가능
		* P66 맨 아래 예
	* set과 list 차이점
		* set은 중복값 삭제되며 순서 고려되지 않음
***

###### 2.4.4 프로퍼티 세팅
* 프로퍼티 주입이 스프링 XML에서 어떻게 작동하는지를 알아본다.
* 새로운 프로퍼티 주입 CDPlayer
	* P67 하단 예
* p-네임스페이스
	* propery 요소의 대안으로 사용 가능
	```XML
    	<bean id="cdPlayer" class="soundsystem.CDPlayer"
        	p:compactDisc-ref="compactDisc" />
    ```
    > 컬렉션을 와이어링할때는 사용할 수 없음
***

#### 2.5 설정 가져오기와 믹싱하기
* JavaConfig 또는 XML 설정에서 컴포넌트 스캐닝 및 오토와이어링을 혼합하는 건 자유
***

###### 2.5.1 JavaConfig로 XML 설정 참조하기
* CDPlayerConfig를 두개로 분할
	* P73 하단 예
	* @Import 어노테이션 사용
	* CDPlayerConfig에서 @import를 남김
		* 두 설정을 함께 가지는 더 높은 수준의 설정파일 생성
* XML import
	* @ImportResource 사용 - P75
***

###### 2.5.2 XML 설정에서 JavaConfig 참조하기
* &lt;import&gt; 요소를 사용해서 XML 설정을 분할
	> XML 분리는 import resource="cd-config.xml" 사용
	> JavaConfig 사용은 &lt;bean class="soundsystem.CDConfig" /&gt;
	> P76 하단 예
***

#### 2.6 요약
* 스프링에 빈을 와이어링하는 세가지 방법
	* 자동 설정
	* 명시적인 Java 기반 설정
	* 명시적인 XML 기반 설정
* 자동설정을 추천
* Java 기반 설정을 추천
***

## 3장 고급 와이어링
* 다룰내용
	* 스프링 프로파일
	* 조건 빈 선언
	* 오토와이어링과 애매성
	* 빈 범위
	* 스프링 표현 언어
***

#### 3.1 환경과 프로파일
* 소프트웨어 개발에서 가장 어려운일 - 다른 환경으로 마이그레이션하는 것
* 각 환경마다 다른 설정
	* 개발환경
		* EmbeddedDatabaseBuilder - P80
	* 리얼환경
		* JNDI를 사용해 DataSource구함 - P80
	* QA환경
		* 완전히 다른 선택 - DBCP 연결풀 구성 - P81
	* 문제점
		* 단순한 DataSource 설정이라도 실제로는 그리 간단하지 않다.
		* 환경에 따라 가장 적절한 설정을 선택하도록 DataSource 빈 설정 방법을 결정해야 함
	* 해결 방법
		* 개별 설정파일에 각 빈을 구성하고, 메이븐 프로파일을 사용하여 빌드 타임에 적절한 설정 파일을 결정
			* 애플리케이션을 각 환경에 맞춰 재구성(빌드) 해야 함
			* QA와 리얼 환경에서 재작성이 필요한 버그가 발생할 수 있음
			* QA팀 구성원 간에 문제도 발생할 수 있음
			* 스프링은 재구성(빌드)를 하지 않는 해결책이 있음 -> 프로파일
***

###### 3.1.1 빈 프로파일 설정하기
* 환경 관련 스프링 솔루션 for 빈
	* 빌드 시에 결정하지 않고 런타임 시에 결정을 내릴때까지 기다림
	* 동일한 WAR 파일이 재구성(빌드)되는 일 없이 모든 환경에서 작동 됨
* 빈 프로파일
	* 스프링 3.1부터 도입 (클래스 단위), 3.2부터는 메소드 단위까지 지원
	* 하나 이상의 프로파일에 모든 다양한 빈 정의를 수집하여 응용 프로그램이 각 환경에 배포될 때 적절한 프로파일의 활성화 여부를 확인해야 함
	* Java 설정에서는 @Profile 어노테이션을 사용
	* 하나의 설정 클래스에 두 빈 선언 결합
		* P83 코드3.1 활성화 파일에 기반을 둔 빈을 와이어링하는 @Profile 어노테이션
		* 프로파일이 주어지지(명시되지) 않은 모든 빈은 항상 생성된다.
***

* XML로 프로파일 설정하기
	* P85 코드3.2 다중 프로파일을 지정하기 위해 반복되는 &lt;beans&gt; 요소
		* dev, qa, prod 프로파일별 빈 설정
		* 런타임 시에는 프로파일이 활성 상태인지에 따라 오직 하나의 빈만 만들어 짐
		* 그럼 프로파일 활성화는 어떻게 할까?
***

###### 3.1.2 프로파일 활성화하기
* 어느 프로파일이 활성상태인지 결정할 때 두가지 프로퍼티를 참고함
	* spring.profiles.active
		* 어느 프로파일이 활성상태인지 결정
	* spring.profiles.default
		* active값이 셋팅되지 않았을때 참조
    * 두 값이 모두 설정되어 있지 않으면
    	* 활성상태 프로파일은 없으며, 프로파일에 정의되지 않은 빈만 생성됨
    * 프로퍼티를 설정하기 위한 여러 방법
    	* DispatcherServlet의 초기화 파라미터로
    	* 웹 애플리케이션의 컨텍스트 파라미터로
    	* JNDI 엔트리로
    	* 환경변수로
    	* JVM 시스템 프로퍼티로
    	* 통합 테스트 클래스에서 @ActiveProfiles 어노테이션을 사용해서
    * 저자의 선호 방법
    	* DispatcherServlet과 서블릿 컨텍스트에서 파라미터를 사용하여 개발 프로파일에spring.profiles.default를 설정하는 것
    	* P87 코드3.3 웹 애플리케이션의 web.xml파일에서 기본 프로파일 설정하기
	    	* 모든 개발자가 추가설정없이 development settings를 사용할수 있음
        * profiles는 복수개를 지정할 수 있다.
***

* 프로파일 테스팅
	* 통합테스트 시, 리얼과 같은 설정을 적용하고 싶을때
	* 설정이 프로파일에 있는 빈을 참조한다면, 테스트를 수행하기 위해 적절한 프로파일을 enable 시킬수 있어야 한다.
	* @ActiveProfile 어노테이션
		* 테스트를 수행할때 활성화할 프로파일을 지정
	* 스프링 프로파일은 활성화된 빈을 기반으로 선택적으로 빈을 정의하는 좋은 방법이지만,
	* Spring4 부터는 더욱 범용적인 목적을 위한 @Conditional 어노테이션을 제공한다.
***

#### 3.2 조건부 빈
* 특정 라이브러리가 사용가능하거나, 특정 환경변수가 설정되어 있을때 특정 빈을 설정하고 싶다면.
* @Conditional 어노테이션
	* 소정의 조건이 참으로 평가된 경우 빈이 생성되고 그렇지 않으면 빈은 무시된다.
	```JAVA
    @Bean
    @Conditional(MagicExistsCondition.class)	<-- 조건적으로 빈 생성
    public MagicBean magicBean(){
    	return new MagicBean();
    }
    ```
	> @Conditional - 조건을 지정하는 클래스를 명시

	* P89 코드3.5 Condition에 magic 존재 여부 체크
		* 환경 변수에 magic 프로퍼터가 존재하는지 확인하는 Condition 코드
		* Condition 인터페이스를 구현하며 matches 메소드를 작성해야 함
	* ConditionContext로 할수 있는 기능 - 인터페이스로 matches에 파라미터로 주어짐
		* 빈 정의 체크
		* 빈의 존재여부를 확인하고, 빈의 속성들도 검증 가능
		* 환경 변수의 존재여부와 값을 확인
		* 자원들의 내용을 읽고, 검사
		* 클래스를 로드하고 체크
	* AnnotatedTypeMetadata - @Bean 메소드의 어노테이션을 검사할수 있게 지원
		* P91 AnnotatedTypeMetadata 인터페이스 정의
	* @Profile 어노테이션은 스프링4에서 @Conditional과 Condition inteface를 기반으로 리팩토링 되었음
		* ProfileCondition에서 Matches를 사용해서 여러가지 조건을 체크하는 형태로 구현 됨
		* P92 코드3.6
***

#### 3.3 오토와이어링의 모호성
* 일치하는 빈이 여럿 있으면, 오토와이어링이 동작하기 어렵다.
* 오토와이어링 모호성 예시
	```JAVA
    @Autowired
    public void setDessert(Dessert dessert){
    	this.dessert = dessert;
    }
    ```
    > setDessert() 메소드가 어노테이션 됨
    > Dessert는 인터페이스이며 세 개의 클래스로 구현 됨

	```JAVA
    @Component
    public class Cake implements Dessert { ... }
    
    @Component
    public class Cookies implements Dessert { ... }
    
    @Component
    public class IceCream implements Dessert { ... }
    ```
	> 세 구현체는 @Component으로 어노테이션 됨
	> 이때 autowiring하면 NoUniqueBeanDefinitionException이 발생 됨

* 모호성 방지를 위한 몇가지 옵션들
	* 후보 빈들 중의 하나가 주요한 선택이 되도록 할수 있음
	* 하나의 후보가 선택되도록 한정자를 붙일 수 있음
***

###### 3.3.1 기본 빈 지정
```JAVA
@Component
@Primary
public class IceCream implements Dessert { ... }
```
> 빈 선언 시 기본 빈으로 하나를 지정하여 오토와이어링의 모호함을 피할 수 있음

```JAVA
@Bean
@Primary
public Dessert IceCream() { 
	return new IceCream();
}
```
> 자바 설정으로 명시적으로 IceCream 빈을 선언 
> 또는 XML로도 기본 빈 선언 가능

* 여러 개의 빈을 기본으로 지정하면 기본 빈이 없는 것과 같다.
***

###### 3.3.2 오토와이어링 빈의 자격 (Qualifying autowired beans)
* @Qualifier 어노테이션
	* qualifier로 작업하는 주요한 방법
	* 주입대상 빈을 지정할 주입 지점에 @Autowired나 @Inject와 함께 적용 된다.
	```JAVA
    @Autowired
    @Qualifier("iceCream")
    public void setDessert(Dessert dessert) {
    	this.desert = desert;
    }
    ```
    > IceCream 빈이 setDessert()에 주입되는지 확실시 함
    > Qualifier의 파라미터는 빈의 아이디임

	* 빈의 아이디가 디폴트로 생성된 경우, 클래스명이 리팩토링되거나 하면 오토와이어링이 실패할 수 있다.
***

* 맞춤형 수식자 만들기 (Creating custom qualifiers)
	* Qualifier로 빈 ID를 사용하는 대신 빈에 qualifier를 지정할수 있다.
	* 빈 선언시 @Qualifier 어노테이션을 사용하면 된다.
	```JAVA
    // 빈 선언 시 Qualifier 적용
    @Componenet
    @Qualifier("cold")
    public class IceCream implements Dessert { ... }
    
    // Autowired시 지정한 Qualifier 사용
    @Bean
    @Qualifier("cold")
    public Dessert iceCream() {
    	return new IceCream();
    }
    ```
***

* 맞춤형 수식자 애너테이션 정의하기 (Defining custom qualifier annotations)
	* 일반적인 특성을 공유하는 빈이 여러개 있을 경우는 문제가 됨
		* @Qualifier("cold")를 복수의 빈에 적용하는 경우
	* 추가로 @Qualifier를 지정하는 해결책이 있을수 있음
	```JAVA
    @Component
    @Qualifier("cold")
    @Qualifier("creamy")
    public class IceCream implements Dessert { ... }
	```
    > 하지만 자바는 동일한 유형의 여러 어노테이션을 같은 항목에 사용할 수 없다. - @Qualifier 복수개 사용 불가
    > 해결책은 맞춤형 수식자 어노테이션을 정의하는 것

	* 맞춤형 수식자 어노테이션
	```JAVA
    @Target({ElementType.CONSTRUCTOR, ElementType.FIELD,
             ElementType.mETHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface Colde {}
    ```
    > @Cold 어노테이션 정의

	```JAVA
    @Component
    @Cold
    @Creamy
    public class IceCream implements Dessert { ... }
    ```
    > @Qualifier를 직접 쓰지 않고 @Qualifier를 정의한 custom qualifier annotation을 정의해서 사용
    > @Qualifier 어노테이션을 사용하는 것과 문자열로 수식자를 지정하는 것보다 type-safe 하다.
***

#### 3.4 빈 범위
* 스프링 애플리케이션에서 생성되는 모든 빈은 기본적으로 싱글톤
* 빈이 생성될 수 있는 범위
	* Singleton - 전체 애플리케이션을 위해 빈의 인스턴스가 하나만 생성됨
	* Prototype - 빈이 주입되거나 어플리케이션 컨텍스트에서 검색 될때마다 빈의 인스턴스 하나 생성
	* Session - 세션당 빈의 인스턴스 하나 생성
	* Request - 요청당 빈의 인스턴스 하나 생성
        ```JAVA
        @Component
        @Scope(ConfiguratleBeanFactory.SCOPE_PROTOTYPE)
        public class Notepad { ... }
        ```
        > scope를 변경하기 위해서는 @Scope를 사용하면 된다. (@Component나 @Bean 어노테이션과 함께 사용)
        > 자바 설정에서는 @Bean과 @Scope을 함께 사용한다.  P101 
        > XML에서는 &lt;bean&gt; 요소에 scope 어트리뷰트를 사용한다.

***

###### 3.4.1 요청(request)과 세션 범위(session scope) 작업하기
* 웹어플리케이션에서 주어진 request나 session 범위 내에서 공유하기 위한 빈을 인스턴스화할때 유용한다.
* 장바구니 예
	* 장바구니빈이 싱글톤일때와 프로토타입인 경우 장단점 비교
	* 장바구니 빈은 사용자마다 가져야 하므로, 세션 범위가 알맞음

```JAVA
@Component
@SCope (
	value=WebApplicationContext.SCOPE_SESSION,
    proxyMode=ScopedProxyMode.INTERFACES)
public ShoppingCart cart() { ... }
```
> * 웹 어플리케이션의 세션당 ShoppingCart 빈의 인스턴스를 생성 (해당 세션에 관해서는 싱글톤)
> * ScopedProxyMode.INTERFACES - proxyMode 특성
> > 싱글톤 범위의 빈에 session 또는 reqeust 범위의 빈을 주입할때 발생하는 문제 해결

```JAVA
@Component
public class StoreService {
	@Autowired
    public void setShoppingCart(ShoppingCart shoppingCart) {
    	this.shoppingCart = shoppingCart;
    }
    ...
}
```
> 어플리케이션 컨텍스트가 로드될때 StoreService 빈이 싱글톤으로 생성 됨
> 그리고 스프링이 ShoppingCart를 주입하려고 하지만 세션 범위이므로 존재하지 않아서 주입이 안된다.
> 스프링은 실제 ShoppingCart를 주입하는 대신에 프록시를 주입한다. P103 그림3.1

* ScopedProxyMode.INTERFACES
	* 프록시가 ShoppingCart의 인터페이스를 구현하고 구현 빈에 위임할 필요가 있다는 의미

* ShoppingCart가 구현클래스인 경우
	* 스프링이 인터페이스 기반의 프록시를 만들수 없다.
	* class-based 프록시를 만들기 위해서는 CGLIB를 사용해야 한다.
	* proxyMode를 ScopedProxyMode.TARGET\_CLASS로 설정해야 한다.

* request scope 도 session scope와 동일하게 와이어링 문제를 야기하므로 scoped proxy로 inject 되어야 한다.
***

###### 3.4.2 XML로 범위 프록시 선언하기 (Declaring scoped proxies in XML)
* XML에서는 @Scope와 proxyMode 어트리뷰트를 사용할수 없다.
* AOP namespace의 새로운 요소를 사용해야 한다.
```XML
<bean id="cart" class="com.myapp.ShoppingCart" scope="session">
	<aop:scoped-proxy />
</bean>
```
> XML에서 @Scope 어노테이션의 proxyMode 어트리뷰트에 대한 대안
> 빈에 대해 범위 프록시를 생성
> 기본적으로 타겟 클래스 프록시를 생성하기 위해 CGLIB를 사용한다.
> proxy-target-class를 false로 해서 인터페이스 기반의 프록시를 생성할 수 있다.
***

#### 3.5 런타임 값 주입
* 다른 빈의 프로퍼티나 생성자 인자에 빈 참조를 주입하는 것
	* 한 개체와 다른 개체의 연결에 관한 것
* 2장에서는 빈의 프로퍼니타 인자로 하드코딩된 값을 전달했으나 런타임 시에 값을 결정하게 할 수 있다.
* 스프링이 제공하는 런타임에 값을 평가하는 두 가지 방법
	* Property placeholders
	* SpEL, Spring Expression Language
***

###### 3.5.1 외부 값 주입
* 프로퍼티 소스를 선언하고 스프링 환경을 통한 프로퍼티 검색이 제일 간단함
	* P106 코드3.7
		* 프로퍼티 소스 선언
		* 프로퍼티 값 조회
***

* 스프링 환경 더 살펴보기
	* Environment의 프로퍼티 값을 조회하는 getProperty()의 네가지 변형
		* String getProperty(String key)
			* 항상 스트링을 반환
		* String getProperty(String key, String defaultValue)
			* 값이 없을 경우 디폴트값을 지정
		* T getProperty(String key, Class&lt;T&gt; type)
			* 스트링 이외의 다른 타입에 대한 처리
		* T getProperty(String key, Class&lt;T&gt; type, T defaultValue)
			```JAVA
            int connectionCount = env.getProperty("db.connection.count", Integer.class, 30);
            ```
        * 프로퍼티가 정의되어 있지 않다면 null을 리턴 받게 됨

    * getRequiredProperty()
		* 프로퍼티가 정의되어 있지 않은 경우 IllegalStateException이 발생
	* containsProperty()
		* 프로퍼티 존재 확인
	* getPropertyAsClass()
		* 프로퍼티를 클래스로 변환
	* Environment가 제공하는 프로파일 관련 메소드
		* String[] getActiveProfiles()
		* String[] getDefaultProfiles()
		* boolean acceptsProfiles(String... profiles)
***

* 프로퍼티 플레이스홀더 처리하기
	* 프로퍼티를 프로퍼티 화일로 관리하며, 플레이스홀더값을 사용하여 빈에 플러그인 한다.
	```XML
    <bean id="sgtPeppers"
    		class="soundsystem.BlankDisc"
            c:_title="${disc.title}"
            c:_artist="${disc.artist}" />
    ```

	* 어플리케이션 컴포넌트를 생성하거나 초기화하기 위해 컴포넌트 스캔과 오토와이어링에 의존한다면, 거기에는 placeholders를 명시할 서ㄹ정파일이나 클래스가 없다.
		* 대신에 @Value를 사용한다.
	* placeholder 값을 사용하려면 빈을 등록해야 한다.
		* PropertySourcePlaceholderConfigurer가 선호된다.
		* P109 자바설정 사용시 빈 등록
		* P109 XML설정 사용시 빈 등록
	* 외부 프로퍼티 해결책은 런타임 시까지 값의 결정을 미루기 위한 방법
		* 스프링의 Environment와 프로퍼티 소스에서 이름으로 프로퍼티들을 잘 결정할 수 있도록 되어 있다.
		* SpEL은 반면에 런타임 시 값을 주입하는 보다 일반적인 방법을 제공
***

###### 3.5.2 스프링 표현식 와이어링 (Wiring with the Spring Expression Language)
* 런타임 시에 평가하는 표현식을 사용해서 빈의 프로퍼티와 생성자 아규먼트에 값을 와이어링하는 SpEL
* 다음 기능을 포함
	* ID로 빈을 참조하는 기능
	* 메소드 호출과 객체의 프로포티 액세스
	* 값에 대한 수학적, 관계적, 논리적 연산
	* 정규 표현식 매칭
	* 컬렉션 처리
* 종속객체 주입 외에 다른 목적으로도 사용 - 보안 정의, 모델 데이타 참조 표현식 등
***

* SpEL 예제
	* 프레임이 #{ ... } 형식으로 구성
	* 플레이스홀더는 ${ ... } 형식으로 만들어짐
	```XML
    #{1}	-- 가장 간단한 SpEL
    ```
	```XML
    #{T(System).currentTimeMillis()}		-- 현재 시간을 밀리초 단위로 나타냄
    ```
	> T()연산자 - java.lang.System의 타입에 대해 평가
	> static currentTimeMillis() 메소드를 수행

	```XML
	#{sgtPeppers.artist}		-- ID가 sgtPeppers인 빈의 artist 프로퍼티 값을 평가
    ```
    > 해당 빈에서 다른 빈 또는 프로퍼티를 참조 

	```XML
    #{systemProperties['disc.title']}
    ```
    > systemProperties 객체를 통해 시스템 프로퍼티를 참조

* 빈 와이어링 동안 SpEL 사용 방법
	* 컴포넌트 스캐닝을 통해 생성되는 빈들에게 프로퍼티를 주입하거나 생성자 인수를 주입할 때,
		* @Value 어노테이션 사용
		* SpEL도 사용 가능
		```JAVA
        public BlankDisc(
        	@Value("#{systemProperties]'disc.title']{") String title,
            @Value("#{systemProperties]'disc.artist']{") String artist) {
            this.title = title;
            this.artist = artist;
        }
        ```
        > 시스템 프로퍼티를 통해 앨범 타이틀과 아티스트를 얻음

		```JAVA
        <bean id="sgtPeppers"
        	class="soundsystem.BlankDisc"
            c:_title="#{systemProperties['disc.title']}"
            c:_artist="#{systemProperties['disc.artist']}" />
        ```
        > BlankDisc 빈의 XML 선언
***

* 리터럴 값들 표시하기
	* 리터럴 정수형, 부동소수점수, String값, 불리언 값등이 표기 가능
	```JAVA
    #{3.14159}	- 부동소수점 값의 SpEL 예
    ```
  	```JAVA
    #{9.87E4}	- 숫자는 과학적 표기법으로 표기, 98,700
    ```
   	```JAVA
    #{'Hello'}	- 리터럴 String 값
    ```
   	```JAVA
    #{false}	- 불리언 리터럴
    ```
	* 좀 더 복잡한 표현식을 나타내기 위해서는 SpEL 표현식이 필요
***

* 빈, 프로퍼티, 메소드 참조
	* SpEL 표현식의 또 다른 역할
		* ID를 이용한 다른 빈의 참조
		```JAVA
        #{sgtPeppers}
        ```
        > ID가 sgtPeppers인 빈을 와이어링

		```JAVA
        #{sgtPeppers.artist}
        ```
        > ID가 sgtPeppers인 빈의 artist 프로퍼티를 참조

		```JAVA
        #{artistSelector.selectArtist()}
        ```
        > ID가 artistSelector인 빈의 selectArtist() 메소드 호출

		```JAVA
        #{artistSelector.selectArtist()?.toUpperCase()}
        ```
        > ID가 artistSelector인 빈의 selectArtist() 메소드 호출 후 null이 아니면 toUpperCase() 실행
***

* 표현식에서 타입 사용하기
	* T() 연산자
	```JAVA
    T(java.lang.Math)
    ```
    > Math class를 의미
    > Class 타입의 빈 프로퍼티에 이 값을 할당할수 있음
    > T() 연산자의 실제 값으로 해당 클래스의 정적 메소드와 상수를 액세스할수 있음

	```JAVA
    T(java.lang.Math).PI
    ```
    > pi 값을 빈 프로퍼티에 와이어링

	```JAVA
    T(java.lang.math).random()
    ```
    > 임의의 숫자(0에서 1사이)를 빈 프로퍼티에 할당하는 방법
***

* SpEL 연산자
	* 표현식에서 값에 적용할 수 있는 다양한 연산을 제공
		* P115 표3.1

	```JAVA
    #{2 * T(java.lang.Math).PI * circle.radius}
    ```
    > pi값에 2를 곱한 값을 circle빈의 radius 프로퍼티값과 곱함

	```JAVA
    #{counter.total == 100}
    ```
    > 두 개의 숫자가 동일한지를 비교하기 위해 이중등호 연산자를 사용

	```JAVA
    #{counter.total eq 100}
    ```
    > ==과 동일
    > 표현식은 불리언으로 결과가 표시 됨

	```JAVA
    #{scoreboard.score > 1000 ? "Winner!" : "Loser"}
    ```
    > 삼항연산자
    > 엘비스 연산자 - ?: 형태
***

* 정규 표현식 사용하기
	* 텍스트가 특정 패턴과 일치하는지 여부 확인을 위한 matches 연산자 제공
	* Matches 평가 결과는 불리언 값
	```JAVA
    #{admin.email matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com'}
    ```
    > String이 유효한 이메일 주소를 포함하는지 체크
***

* 컬렉션 사용하기
	* SpEL은 컬렉션과 배열을 이용하여 작업 가능
	```JAVA
    #{jukebox.songs[4].title}
    ```
    > 아이디가 jukebox인 빈의 songs 컬렉션 프로퍼티의 다섯번째 요소의 title 프로퍼티를 조회

	```JAVA
    #{jukebox.songs[T(java.lang.Math).random() * jukebox.songs.size()].title}
    ```
    > jukebox에서 song 하나를 무작위로 선택하는 경우

	* selection 연산자
		* 컬렉션을 컬렉션 서브셋으로 필터링 할때 사용
		* .?[]
            ```JAVA
            #{jukebox.songs.?[artist eq 'Aerosmith']}
            ```
            > jukebox의 song 전체 리스틍에서 artisst가 Aerosmith인 노래를 필터링

		* .^[], .$[] 연산자
			* 컬렉션에서 첫번째와 마지막에 일치하는 항목을 조회
		* .![] 연산자
			* 컬렉션의 요소로부터 프로퍼티를 새로운 컬렉션으로 모음
			```JAVA
            #{jukebox.songs.?[artist eq 'Aerosmith'].![title]}
            ```
            > Aerosmith의 모든 노래의 타이틀을 새로운 컬렉션으로 조회
***

#### 3.6 요약
* 스프링 프로파일 사용
	* 빈은 하나 이상의 활성화된 프로파일에 매칭시켜 런타임 시의 고유 환경 빈 사용시의 문제를 해결 함
	* 런타임 시 빈을 조건부로 만드는 방법

* @Conditional 어노테이션
	* 주어진 조건의 결과에 따라 빈을 생성/비생성 하는 일반적인 방법
	* 스프링4가 제공하는 더욱 일반적 방법

* 오토와이어링의 애매함을 해결하기 위한 방법
	* primary beans
		* 간단하지만 제한적
	* qualifiers
		* 오토와이어의 후보 대상을 좁혀주는 역할
	* 커스텀 qualifier 어노테이션 생성 방법

* 빈의 scope
	* 싱글톤 scope
	* 프로토타입 scope
	* reqeust scope
	* session scope

* Spring Expression Language
***

## 4장 애스펙트 지향 스프링
* 다룰내용
	* Basics of aspect-oriented programming
	* POJO를 사용하여 애스펙트 만들기
	* @AspectJ 어노테이션 사용하기
	* AspectJ 애스펙트에 종속객체 주입하기

* 한 기능이 애플리케이션 여러곳에 적용되어야 할 경우, 적용 부분마다 이 기능을 호출하는 일을 바람직하지 않다.
* 횡단 관심사 (cross-cutting concerns)
	* 한 애플리케이션의 여러 부분에 걸쳐 있는 기능
	* 비즈니스 로직과는 분리되는 기능
	* AOP는 이러한 횡단 관심사의 분리를 위한 것임

* DI의 목적
	* 객체 간 결합도를 낮추는 것
* AOP의 목적
	* 횡단 관심사와 이에 영향 받는 객체 간 결합도를 낮추는 것
***

#### 4.1 AOP란 무엇인가?
* 횡단 관심사
	* 한 애플리케이션의 여러 부분에 영향을 주는 기능
		* 보안, 로깅, 트랜잭션, DB Connection 등
		* P123 그림4.1
* 공통 기능의 재사용
	* 상속, 위임
	* AOP
		* 전체 코드 기반에 흩어져 있는 주요 관심 사항이 하나의 장소로 응집된다는 장점
		* 서비스 모듈이 자신의 주요 관심 사항에 대한 코드만 포함하면 됨
***

###### 4.1.1 AOP 용어 정의
* P124 그림4.2
* 어드바이스
	* 애스펙트가 해야하는 작업과 언제 그 작업을 수행해야 하는지를 정의한 것
	* 종류
		* before, after, after-returning, after-throwing, around의 어드바이스가 존재
* 조인 포인트
	* 어드바이스를 적용할 수 있는 곳, 어플리케이션에 애스펙트를 끼워넣을 수 있는 지점
	* 메소드 호출 지점, 예외발생, 필드 값 수정 부분 등
* 포인트컷
	* 애스펙트가 '어디서' 할지를 정의
	* 클래스나 메소드 명을 직접 지정하거나 매칭 패턴을 나타내는 정규식으로 정의
* 애스펙트
	* 어드바이스와 포인트컷을 합친 것
* 인트로덕션
	* 기존 클래스에 코드 변경 없이 새 메소드가 멤버 변수를 추가하는 기능
	* 스프링 Roo에서 많이 활용된다고 함
* 위빙
	* 타겟 객체에 애스펙트를 적용해서 새로운 프록시 객체를 생성하는 절차
***

###### 4.1.2 스프링의 AOP 지원
* 스프링의 AOP 지원 형태
	* Classic Spring proxy-based AOP
	* Pure-POJO aspects
	* @AspectJ annotation-driven aspects
	* Injected AspectJ aspects (스프링 전 버전에서 사용 가능)
* 스프링 프록시
	* 동적 프록시 기반으로 만들어짐
	* 메소드 interception으로만 제한 됨
	* 메소드 interception 이상의 기능이 필요하면 AspectJ를 이용하여 애스펙트를 구현해야 함
***

* 스프링 어드바이스는 자바로 작성
	* 모든 어드바이스는 표준 자바 클래스로 작성
	* 포인트컷은 보통 XML 설정 파일에 정의
* 스프링 애스펙트
	* P128 그림4.3 
* 실행시간에 만드는 스프링 어드바이스
	* 빈을 감싸는 프록시 객체를 실행 시간에 생성한다.
	* 프록시 객체는 타겟 객체로 위장해서 어드바이스 대상 메소드의 호출을 가로채고, 타겟 객체로 호출을 전달한다.
* 스프링은 메소드 조인 포인트만 지원
	* AspectJ나 다른 AOP 프레임워크는 
		* 플드나 생성자 조인 포인트까지 제공
***

#### 4.2 포인트컷을 이용한 조인 포인트 선택
* 스프링에서 포인트컷은 AspectJ의 포인트컷 표현식 언어를 사용
* 스프링은 AspectJ에서 사용할 수 있는 포인트컷 지정자에 속하는 것만 지원한다.
* 스프링 AOP에서 지원되는 AspectJ 포인트컷 지정자
	* P130 표4.1
	* AspectJ의 다른 지정자를 사용하면 IllegalArgumentException이 발생
***

###### 4.2.1 포인트컷 작성
* 애스펙트를 나타내기 위해 애스펙트 포인트컷 대상을 정의
	* Performance 인터페이스 - 영화 , 콘서트와 같은 공연을 나타냄
	* perform() 메소드를 트리거링 하는 애스펙트를 작성
	* P131 그림4.4 perform() 메소드가 실행될때마다 어드바이스 하기 위한 포인트컷 표현식
	```XML
    execution (* concert.Performance.perform(..))
    ```
    > 파라미터 갯수와 리턴 타입에 상관없이 perform 메소드에 대해서 적용하라는 의미

	```XML
    execution (* concert.Performance.perform(..)) && within(concert.*)
    ```
    > 포인트컷 범위를 concert 패키지로 제한
***

###### 4.2.2 포인트컷에서 빈 선택하기
* bean() 지정자
	```XML
    execution(* concert.Performance.perform()) and bean('woodstock')
    ```
    > ID가 woodstock인 빈으로 제한
***

#### 4.3 애스펙트 어노테이션 만들기
* AspectJ 5에 도입된 주요 기능
	* 어노테이션을 사용하여 애스펙트를 만들 수 있는 기능
	* 어노테이션을 조금 사용해 어떤 클래스든 간단하게 애스펙트로 바꿀 수 있게 됨
		* @AspectJ 어노테이션
***

###### 4.3.1 애스펙트 정의하기
* 관객을 공연에 적용할 수 있는 애스펙트로 정의
* P133 코드4.1 Audience 클래스
	* @Aspect라는 어노테이션이 붙어 있음 - POJO가 아니라 애스펙트라는 의미
	* P134 표4.2 - 어드바이스용 어노테이션 5가지
	* Performance의 perform() 메소드 실행이 트리거가 됨
* 한번만 포인트컷을 정의하고 필요할때마다 참조하는 방법
	* 재사용 가능한 포인트컷 정의
	```JAVA
    @Pointcut("execution(** concert.Performance.perform(..))")
    public void performance(){)
    
    @Before("performance()") <-- 이런 형태로 사용(참조형태)
    ```

* 애스펙트 사용 순서
	1. @Aspect 어노테이션이 붙은 빈 정의
	2. @Aspect 클래스 안에 @Before, @After 등의 어노테이션(어드바이스)이 붙은 메소드 작성
	3. @Bean을 사용해서 와이어링
	4. 설정 사용
		4-1 JavaConfig
			@EnableAspectJAutoProxy 어노테이션을 통해 오토-프록싱을 사용 - P136 코드4.3
        4-2 XML 설정
        	aop 네임스페이스에서 <aop:aspectj-autoproxy> 사용 - P137 코드4.4

* @AspectJ 어노테이션을 사용하더라도 여전히 메소드 참조 프록싱만 사용할수 있는 제약점이 있다.
* 더 많은 AOP의 기능을 사용하려면 AspectJ 런타임을 사용해야 한다.
***

###### 4.3.2 around 어드바이스 만들기
* 하나의 어드바이스 메소드 내에서 before와 after 어드바이스 모두를 작성할때 필수적이다.
* P138 코드4.5 before와 advice 대신 around 하나로 재작성
	> 파라미터로 ProceedingJoinPoint의 proceed 메소드를 호출해야 함
***

###### 4.3.3 어드바이스에서 파라미터 처리하기
* 앞의 예제에서 BlankDisc 클래스를 다시 살펴 봄
	* 각 트랙이 얼마나 많이 호출되었는지 세기 원함
	* playTrack()을 어드바이스하는 애스펙트인 TrackCounter를 생성
		* P140 코드4.6 트랙 재생횟수를 카운트하기 위해 파라미터화된 어드바이스 사용하기
		* 명명된 포인트컷을 사용하기 위해 @Pointcut을 사용
		* 어드바이스 전에 적용되는 메소드를 선언하기 위해 @Before를 사용
***

###### 4.3.4 인트로덕션 어노테이션
* 오픈클래스
	* 루비나 그루비 등에서 지원하는 객체나 클래스의 정의를 변경하지 않고 실행 중에 새로운 메소드를 추가하는 기능
* AOP에서는 객체의 기존 메소드 주위(around)에 새로운 기능을 추가했음
	* AOP 개념을 이용해, 애스펙트는 스프링 빈에 새로운 메소드를 추가 함
	* P143 그림4.7
* 추가된 인터페이스의 메소드가 호출되면, 프록시는 새로운 인터페이스의 구현체를 제공하는 다른 객체에 호출을 위임한다.
	```JAVA
    package concert;
    
    public interface Encoreable {
    	void performEncore();
    }
    ```
***

## 5장 스프링 웹 애플리케이션 만들기
* 다룰내용
	* 요청을 스프링 컨트롤러에 매핑하기
	* 파라미터 폼을 투명하게 바인딩하기
	* 제출 폼 검증하기
***

#### 5.1 스프링 MVC 시작하기
* 스프링 MVC가 클라이언트로부터 요청을 받아 클라이언트에 돌려주는 과정을 살펴보자.
***

###### 5.1.1 스프링 MVC를 이용한 요청 추적
* P162 그림5.1 원하는 결과를 얻기 위한 여러 단계에 대한 요청 처리 정보
	* 요청1
		* 사용자가 요구하는 내용을 전달 - Dispatcher가 받음
		* 프론트 컨트롤러 서블릿
	* 요청2
		* 요청을 처리할 컨트롤러를 찾아주는 핸들러 매핑
	* 요청3
		* 요청을 처리할 컨트롤러
	* 요청4
		* 모델과 논리뷰 이름을 DispatcherServlet에게 리턴
	* 요청5
		* 뷰 리졸버에게 논리적으로 주어진 뷰의 이름과 실제로 구현된 뷰를 매핑 처리
	* 요청6
		* 뷰의 구현
	* 요청7
		* 클라이언트에게 응답
***

###### 5.1.2 스프링 MVC 설정하기
* 스프링 MVC를 설정하는 간단한 접근법을 살펴보자
* DispatcherServlet 설정하기
	* 이전에는 web.xml로 설정을 했으나 Java를 사용
	* P165 코드5.1 DispatcherServlet 설정하기
	```JAVA
    package com.chap05.spittr.config;

    import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

    public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

        protected Class<?>[] getRootConfigClasses() {
            return new Class<?>[] {
                    RootConfig.class
            };
        }

        protected Class<?>[] getServletConfigClasses() {	<-- 설정 클래스를 명시
            return new Class<?>[] {
                    WebConfig.class
            };
        }

        protected String[] getServletMappings() {		<-- DispatcherServlet을 /에 매핑
            return new String[] {
                "/"
            };
        }
    }
    ```
    > AbstractAnnotationConfigDispatcherServletInitializer를 상속받으면 됨

* 두 애플리케이션 컨텍스트에 대한 이야기
	* DispatcherServlet
		* 컨트롤러, 뷰 리졸버, 핸들러 매핑과 같은 웹 컴포넌트가 포함된 빈을 로딩
		* getServletConfigClasses에서 리턴된 설정 클래스들이 관련 빈들을 정의하는데 사용
	* ContextLoaderListener
		* 애플리케이션 내의 그 외의 다른 빈을 로딩
		* getRootConfigClass에서 리턴된 설정 클래스들이 관련 빈들을 정의하는데 사용
	* 클래스를 사용한 DispatchetServlet 설정방식
		* 톰캣7 이상에서만 동작됨
***

* 스프링 MVC 활성화하기
	* XML
		* &lt;mvc:annotation-driven&gt; 이 활성화를 위해 사용
	* JavaConfig
		* WebConfig
            * @EnableWebMvc 사용
            ```JAVA
            package com.chap05.spittr.config;

            import org.springframework.context.annotation.Configuration;
            import org.springframework.web.servlet.config.annotation.EnableWebMvc;

            @Configuration
            @EnableWebMvc
            public class WebConfig {

            }
            ```
            > 더 해야할 사항
            >> 뷰 리졸버 설정 - 디폴트는 BeanNameViewResolver를 사용함
            >> 컴포넌트 스캔 활성화 필요
            >> 고정 리소스들에 대한 모든 요청을 처리하고 있으므로 분리 필요

            * 추가 설정
            ```JAVA
            package com.chap05.spittr.config;

            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.ComponentScan;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.web.servlet.ViewResolver;
            import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
            import org.springframework.web.servlet.config.annotation.EnableWebMvc;
            import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
            import org.springframework.web.servlet.view.InternalResourceViewResolver;

            @Configuration
            @EnableWebMvc
            @ComponentScan("com.chap05.spittr")
            public class WebConfig extends WebMvcConfigurerAdapter {
                @Bean
                public ViewResolver viewResolver() {
                    InternalResourceViewResolver resolver = new InternalResourceViewResolver();

                    resolver.setPrefix("/WEB-INF/views/");
                    resolver.setSuffix(".jsp");
                    resolver.setExposeContextBeansAsAttributes(true);
                    return resolver;
                }

                public void configureDefaultServeletHandling(DefaultServletHandlerConfigurer configurer) {
                    configurer.enable();
                }
            }
            ```
            > @ComponentScan 추가됨
            > ViewResolver 추가됨
            > configureDefaultServletHandling() - enable()을 호출함으로써 DispatcherServlet이 고정적인 리소스들에 대한 요청을 자신이 처리하지 않고 서블릿 컨테이너의 디폴트 서블릿에 전달하도록 한다.
		* RootConfig
			* 이 챕터에서는 간단한 설정만 사용하자.
			```JAVA
            package com.chap05.spittr.config;

            import org.springframework.context.annotation.ComponentScan;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.context.annotation.FilterType;
            import org.springframework.web.servlet.config.annotation.EnableWebMvc;

            @Configuration
            @ComponentScan(basePackages = {"com.chap05.spittr"},
                excludeFilters = {
                        @ComponentScan.Filter(type= FilterType.ANNOTATION, value= EnableWebMvc.class)
                })
            public class RootConfig {
            }
            ```
            > 빈 등로을 위해 @ComponentScan을 사용해야 함
***

###### 5.1.3 Spittr 애플리케이션 소개
* 간단한 마이크로 블로깅 서비스 작성
	* spitter(어플리케이션 사용자)와 spittles(사용자가 쓴 상태 업데이트)를 가진다.
	* 웹 레이어 발전 -> spittles를 처리할 컨트롤러 작성 -> spitter 사용자 가입 폼 처리 순서로 개발
***

#### 5.2 간단한 컨트롤러 작성하기
* 간단한 HomeController
	* P171 코드5.3
	```JAVA
    package com.chap05.spittr.web;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    import static org.springframework.web.bind.annotation.RequestMethod.GET;

    @Controller
    public class HomeController {
        @RequestMapping(value = "/", method=GET)
        public String home() {
            return "home";
        }
    }
    ```
    * P172 코드5.4 간단한게 JSP로 정의된 Spittr 홈페이지
    ```XML
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page session="false" %>
    <html>
      <head>
        <title>Spitter</title>
        <link rel="stylesheet"
              type="text/css" 
              href="<c:url value="/resources/style.css" />" >
      </head>
      <body>
        <h1>Welcome to Spitter</h1>

        <a href="<c:url value="/spittles" />">Spittles</a> |
        <a href="<c:url value="/spitter/register" />">Register</a>
      </body>
    </html>
    ```
***

###### 5.2.1 컨트롤러 테스팅
* HomeController는 어노테이션을 배제하면 POJO임
* 테스트코드
	```JAVA
    package com.chap05.spittr.web;

    import org.junit.Test;

    import static org.junit.Assert.assertEquals;

    public class HomeControllerTest {
        @Test
        public void testHomePage() throws Exception {
            HomeController controller = new HomeController();
            assertEquals("home", controller.home());
        }
    }
    ```
    > 단순하게 메소드의 호출 결과만 테스트하고 있음
    > MVC 컨트롤러의 테스트로는 적절하지 않음

* 수정된 테스트 코드
	```JAVA
    package com.chap05.spittr.web;

    import static org.junit.Assert.assertEquals;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
    import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

    import org.junit.Test;
    import org.springframework.test.web.servlet.MockMvc;

    public class HomeControllerTest {
        @Test
        public void testHomePage() throws Exception {
            HomeController controller = new HomeController();
            assertEquals("home", controller.home());
        }

        @Test
        public void testHomePage2() throws Exception {
            HomeController controller = new HomeController();
            MockMvc mockMvc = standaloneSetup(controller).build();
            mockMvc.perform(get("/"))
                    .andExpect(view().name("home"));
        }
    }
    ```
    > HomeController를 확실히 테스트할 수 있음
    > "/"에 GET 요청을 발생시켜 결과 뷰 이름이 home 인지 여부를 체크
***

###### 5.2.2 클래스 레벨 요청 처리 정의하기
* P174 코드 5.7 HomeController의 @RequestMapping 분할하기
    ```JAVA
    package com.chap05.spittr.web;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    import static org.springframework.web.bind.annotation.RequestMethod.GET;

    @Controller
    @RequestMapping({"/", "/homepage"})
    public class HomeController {
        @RequestMapping(method=GET)
        public String home() {
            return "home";
        }
    }
    ```
    > 경로 매핑이 클래스 레벨로 변경 됨 - 모든 메소드에 적용됨
    > 결론적으로 실제 변경된 부분은 없음
    > "/homepage" 맵핑을 추가해 복수개의 맵핑 처리도 가능함
    > 
***

###### 5.2.3 뷰에 모델 데이터 전달하기
* 목록을 보여주는 새로운 메소드 추가
	* 데이터 처리를 위한 저장소 추가
	```JAVA
    package com.chap05.spittr.data;

    import com.chap05.spittr.Spittle;

    import java.util.List;

    public interface SpittleRepository {
        List<Spittle> findSpittles(long max, int count);
    }

    ```
    > max : 리턴되어야 하는 Spittle의 최대 ID값
    > count : 리턴되는 Spittle 객체의 총 갯수

	* Spittle 클래스
		* P176 코드 5.8
		* 메시지, 타임스탬프, 위치의 위도/경도 프로퍼티
***

* 새로운 컨트롤러에 대한 테스트 코드
	* P178 코드5.9
	```JAVA
    package com.chap05.spittr.web;

    import com.chap05.spittr.Spittle;
    import com.chap05.spittr.data.SpittleRepository;
    import org.junit.Test;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.web.servlet.view.InternalResourceView;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    import static org.junit.matchers.JUnitMatchers.hasItems;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

    public class SpittleControllerTest {
       @Test
        public void shouldShowRecentSpittles() throws Exception {
            List<Spittle> expectedSpittles = createSpittleList(20);
            SpittleRepository mockRepository = mock(SpittleRepository.class);

            when(mockRepository.findSpittles(Long.MAX_VALUE, 20))
                    .thenReturn(expectedSpittles);

            SpittleController controller = new SpittleController(mockRepository);
            MockMvc mockMvc = standaloneSetup(controller)
                    .setSingleView(
                            new InternalResourceView("/WEB-INF/vies/spittles.jsp"))
                    .build();

            mockMvc.perform(get("/spittles"))
                    .andExpect(view().name("spittles"))
                    .andExpect(model().attributeExists("spittleList"))
                    .andExpect(model().attribute("spittleList",
                            hasItems(expectedSpittles.toArray())));
        }

        private List<Spittle> createSpittleList(int count) {
            List<Spittle> spittles = new ArrayList<Spittle>();

            for (int i = 0; i < count; i++) {
                spittles.add(new Spittle("Spittle " + i, new Date()));
            }
            return spittles;
        }
    }
    ```
***

* SpittleController
	```JAVA
    package com.chap05.spittr.web;

    import com.chap05.spittr.data.SpittleRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;

    @Controller
    @RequestMapping("/spittles")
    public class SpittleController {
        private SpittleRepository spittleRepository;

        @Autowired
        public SpittleController(SpittleRepository spittleRepository) {
            this.spittleRepository = spittleRepository;
        }

        @RequestMapping(method = RequestMethod.GET)
        public String spittles(Model model) {
            model.addAttribute(spittleRepository.findSpittles(Long.MAX_VALUE, 20));

            return "spittles";
        }
    }
    ```
    > spittles 메소드는 findSpittles에서 리턴받은 리스트를 model에 담아서 리턴한다.
    > addAttribute가 키값 없이 호출되면, 키는 값으로 설정된 객체 타입으로 추측한다. 여기서는 spittList가 된다.
    > model 대신 java.util.Map을 사용해도 된다.

	* 모델 내의 데이터 접근
		* 뷰가 JSP면, 모델 데이터는 request attribute로 request에 복사 된다.
***
#### 5.3 요청 입력받기
* 클라이언트가 서버로 데이터를 전달하는 방법 in 스프링 MVC
	* Query parameters
	* Form parameters
	* Path variables
***

###### 5.3.1 쿼리 파라미터 입력받기
* 페이징 구현을 위해 다음 파라미터 입력이 필요 함
	* A before parameter (which indicates the ID of the Spittle that all Spittle objects in the results are before)
	* A count parameter (which indicates how many spittles to include in the result)

* 페이징 메소드 테스트
	```JAVA
      @Test
      public void shouldShowPagedSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(50);
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findSpittles(238900, 50))
            .thenReturn(expectedSpittles);

        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
            .setSingleView(new InternalResourceView("/WEB-INF/views/spittles.jsp"))
            .build();

        mockMvc.perform(get("/spittles?max=238900&count=50"))
          .andExpect(view().name("spittles"))
          .andExpect(model().attributeExists("spittleList"))
          .andExpect(model().attribute("spittleList", 
                     hasItems(expectedSpittles.toArray())));
      }
    ```
	> spittles에 대한 max와 count 파라미터 값을 넘겨 받는 부분이 추가됨

* 컨트롤러의 목록조회 메소드 수정
	```JAVA
    package com.chap05.spittr.web;

    import com.chap05.spittr.Spittle;
    import com.chap05.spittr.data.SpittleRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RequestParam;

    import java.util.List;

    @Controller
    @RequestMapping("/spittles")
    public class SpittleController {
        private static final String MAX_LONG_AS_STRING = Long.toString(Long.MAX_VALUE);

        private SpittleRepository spittleRepository;

        @Autowired
        public SpittleController(SpittleRepository spittleRepository) {
            this.spittleRepository = spittleRepository;
        }

        @RequestMapping(method = RequestMethod.GET)
        public List<Spittle> spittles(@RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
                                      @RequestParam(value = "count", defaultValue = "20") int count) {
            return spittleRepository.findSpittles(max, count);
        }
    }

    ```
    > @RequestParam이 추가됐음
    > defaultValue를 사용해서 파라미터가 없을 경우는 기본값 사용
***

###### 5.3.2 패스 파라미터를 통한 입력 받기
* 파라미터 입력 방법
	* 쿼리 파라미터 방식
		* /spittles/show?spittle_id=12355와 같은 형태
	* 패스 파라미터 방식
		* /spittles/12345 형태

* P186 코드5.12 패스 변수로 ID를 명시하는 Spittle의 요청에 대한 테스트
	* 테스트코드
	```JAVA
        @Test
        public void testSpittle() throws Exception {
            Spittle expectedSpittle = new Spittle("Hello", new Date());
            SpittleRepository mockRepository = mock(SpittleRepository.class);

            when(mockRepository.findOne(12345)).thenReturn(expectedSpittle);

            SpittleController controller = new SpittleController(mockRepository);
            MockMvc mockMvc = standaloneSetup(controller).build();

            mockMvc.perform(get("/spittles/12345"))
                    .andExpect(view().name("spittle"))
                    .andExpect(model().attributeExists("spittle"))
                    .andExpect(model().attribute("spittle", expectedSpittle));
        }
    ```
    
	* 패스 변수로 받는 메소드 코드
	```JAVA
    @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
    public String spittle (@PathVariable("spittleId") long spittleId, Model model) {
        model.addAttribute(spittleRepository.findOne(spittleId));
        return "spittle";
    }
    ```
	> 패쓰 변수를 받기 위해 플레이스홀더 (중괄호 {와}로 묶인 부분) 사용
	> @PathVariable long spittleId와 같이 value를 생략한 형태도 가능

* 화면 jsp
	```XML
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <html>
      <head>
        <title>Spitter</title>
        <link rel="stylesheet" 
              type="text/css" 
              href="<c:url value="/resources/style.css" />" >
      </head>
      <body>
        <div class="spittleView">
          <div class="spittleMessage"><c:out value="${spittle.message}" /></div>
          <div>
            <span class="spittleTime"><c:out value="${spittle.time}" /></span>
          </div>
        </div>
      </body>
    </html>
    ```

* 장단점
	* 쿼리/패스 파라미터 방식은 작은 데이터 전달에는 적당
	* 폼의 많은 데이터 전달에는 불편하고 제약이 많음
***

#### 5.4 폼 처리하기
* 두가지 과정이 있음
	* 폼 보여주기
	* 폼을 통해 제출한 데이터 처리
* 폼 표시를 위한 컨트롤러 - SpitterController
    ```JAVA
    package com.chap05.spittr.web;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    import static org.springframework.web.bind.annotation.RequestMethod.GET;

    @Controller
    @RequestMapping("/spitter")
    public class SpitterController {
        @RequestMapping(value = "/register", method = GET)
        public String showRegistrationForm() {
            return "registerForm";
        }
    }
    ```

* SpitterController 테스트 코드
	```JAVA
    package com.chap05.spittr.web;

    import org.junit.Test;
    import org.springframework.test.web.servlet.MockMvc;

    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
    import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

    public class SpitterControllerTest {
        @Test
        public void shouldShowRegistration() throws Exception {
            SpitterController controller = new SpitterController();
            MockMvc mockMvc = standaloneSetup(controller).build();
            mockMvc.perform(get("/spitter/register"))
                    .andExpect(view().name("registerForm"));
        }
    ```

* 등록폼 렌더링용 JSP
	* P190 코드5.15 등록 폼을 렌더링하기 위한 JSP
	```XML
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page session="false" %>
    <html>
      <head>
        <title>Spitter</title>
        <link rel="stylesheet" type="text/css" 
              href="<c:url value="/resources/style.css" />" >
      </head>
      <body>
        <h1>Register</h1>

        <form method="POST">
          First Name: <input type="text" name="firstName" /><br/>
          Last Name: <input type="text" name="lastName" /><br/>
          Email: <input type="email" name="email" /><br/>
          Username: <input type="text" name="username" /><br/>
          Password: <input type="password" name="password" /><br/>
          <input type="submit" value="Register" />
        </form>
      </body>
    </html>
    ```
    > action 파라미터가 없으므로 submit하면 동일한 URL이 다시 호출된다.
***

###### 5.4.1 폼 처리 컨트롤러 작성
* 등록 폼에서 POST 요청 -> 컨트롤러는 폼 데이터를 받아서 Spitter 객체로 이 폼을 저장해야 함
* 폼 처리 컨트롤러 테스트 코드
	* P191 코드5.16 폼 처리 컨트롤러를 테스트하는 코드
	```JAVA
        @Test
        public void shouldProcessRegistration() throws Exception {
            SpitterRepository mockRepository = mock(SpitterRepository.class);
            Spitter unsaved = new Spitter("jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
            Spitter saved = new Spitter(24L, "jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
            when(mockRepository.save(unsaved)).thenReturn(saved);

            SpitterController controller = new SpitterController(mockRepository);
            MockMvc mockMvc = standaloneSetup(controller).build();

            mockMvc.perform(post("/spitter/register")
                    .param("firstName", "Jack")
                    .param("lastName", "Bauer")
                    .param("username", "jbauer")
                    .param("password", "24hours")
                    .param("email", "jbauer@ctu.gov"))
                    .andExpect(redirectedUrl("/spitter/jbauer"));

            verify(mockRepository, atLeastOnce()).save(unsaved);
        }
    ```
	> SpitterRepository의 모사 구현의 설정과 컨트롤러를 생성
	> 실행위한 MockMvc를 설정 및 저장 후 리다이렉션도 테스트

* 폼 처리 컨트롤러 - P192 코드5.17 새로운 사용자를 등록하기 위한 폼을 제출하기
    ```JAVA
    package com.chap05.spittr.web;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    import static org.springframework.web.bind.annotation.RequestMethod.GET;
    import static org.springframework.web.bind.annotation.RequestMethod.POST;

    @Controller
    @RequestMapping("/spitter")
    public class SpitterController {
        private SpitterRepository spitterRepository;

        @Autowired
        public SpitterController(SpitterRepository spitterRepository) {
            this.spitterRepository = spitterRepository;
        }

        @RequestMapping(value = "/register", method = GET)
        public String showRegistrationForm() {
            return "registerForm";
        }

        @RequestMapping(value = "/register", method = POST)
        public String processRegistration(Spitter spitter) {
            spitterRepository.save(spitter);

            return "redirect:/spitter/" + spitter.getUsername();
        }
        
        @RequestMapping(value = "/{username}", method = GET)
        public String showSpitterProfile(@PathVariable String username, Model model) {
        	Spitter spitter = spitterRepository.findByUsername(username);
	        model.addAttribute(spitter);
	        return "profile";
        }
    }
    ```
    > 파라미터로 Spitter 객체를 받음 - 요청 파라미터로 값이 채워짐 (커맨트 객체)
    > 리다이렉션 처리를 위한 showSpitterProfile 메소드 추가
***

###### 5.4.2 폼 검증하기
* 폼에서 넘어오는 값을 검증
	* processRegistration() 메소드의 잘못된 값들을 확인하고 사용자에게 다시 폼을 보여주자.
	* 스프링 3.0부터 자바 인증 API가 추가됨 - 하이버네이트 validator형태만 구현하면 됨
	* P195 표5.1 자바 인증 API에서 제공되는 검증 어노테이션 목록
	* Spitter 필드에 @NotNull과 @Size 어노테이션을 추가해보자
	```JAVA
    package com.chap05.spittr;

    import com.sun.istack.internal.NotNull;
    import javax.validation.constraints.Size;

    import org.hibernate.validator.constraints.Email;

    public class Spitter {

        private Long id;

        @NotNull
        @Size(min=5, max=16)
        private String username;

        @NotNull
        @Size(min=5, max=25)
        private String password;

        @NotNull
        @Size(min=2, max=30)
        private String firstName;

        @NotNull
        @Size(min=2, max=30)
        private String lastName;

        @NotNull
        @Email
        private String email;
		...
    ```

	* 검증을 적용을 위해 processRegistration() 수정
	```JAVA
        @RequestMapping(value = "/register", method = POST)
        public String processRegistration(@Valid Spitter spitter, Errors errors) {
            if (errors.hasErrors()) {
                return "registerForm";
            }
            spitterRepository.save(spitter);

            return "redirect:/spitter/" + spitter.getUsername();
        }
    ```
    > 반드시 Errors 파라미터가 검증될 @Valid 어노테이션이 붙은 파라미터 뒤에 와야한다.
***

#### 5.5 요약
* 웹 애플리케이션의 일부를 다루었다.
* 어노테이션을 채용하여 강력하고 유연한 프레임워크로 돌아왔다.
***

## 6장 웹 뷰 렌더링
* 다룰내용
	* HTML으로 모델 데이터 렌더링하기
	* JSP 뷰 사용하기
	* 타일즈로 뷰 레이아웃 정의하기
	* Thymeleaf 뷰로 작업하기
***

#### 6.1 View Resolution 이해하기
* 앞 장의 예제
	* 메소드에서 HTML을 생성하지 않고, 모델에 데이터를 채워서 렌더링을 담당하는 뷰에 전달했음
	* 컨트롤러에서는 세부적인 뷰의 규현에 대해서는 모른다.
	* View Resolver
		* 컨트롤러가 뷰의 이름만 알고 있을때, 모델을 렌더링할때 사용하는 뷰 구현을 결정
* 일반적인 뷰 결정법과 다른 뷰 리졸버를 살펴보자
	```JAVA
    public interface ViewResolver {
    	View resolveViewName(String viewName, Locale locale) throws Exception;
    }
    ```
    > ViewResolver 인터페이스
    
    ```JAVA
    public interface View {
    	String getContentType();
        void render(Map<String, ?> model,
        			HttpServletRequest request,
                    HttpServletResponse response) throws Exception;
    }
    ```
    > View 인터페이스
    > 모델과 서블릿 요청과 응답 객체를 전달받아 결과를 응답에 렌더링

* 즉 view와 viewResolver의 구현체만 개발하면 된다.
* 또는 기 제공되는 구현체를 사용하면 된다.
	* P201 표6.1 스프링은 논리적 뷰 이름을 물리적인 뷰의 구현으로 변환하기 위한 13개의 뷰 리졸버를 제공한다.
		* 각 ViewResolver들은 특정 뷰 기술에 대응한다.
			* InternalResourceResolver = JSP
			* TilesViewResolver = 아파치 Tiles
			* FreeMarkerViewResolver = Freemarker
			* VelocityViewResolver = Velocity
***

#### JSP 뷰 만들기
* 스프링은 다음 두 가지 방식으로 JSP를 지원
	* InternalResourceViewResolver
		* JSP 파일에 뷰 이름을 결정하기 위해 사용
	* form-to-model 바인딩을 위한 것과 일반적인 유틸리티 기능을 제공하는 JSP 태그 라이브러리를 제공
***

###### 6.2.1
* JSP-ready 뷰 리졸버 설정
	* 접두사와 접미사를 붙이는 것으로 뷰의 이름을 결정
		* "/WEB-INF/views" + "/home" + ".jsp"
	```JAVA
        @Bean
        public ViewResolver viewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();

            resolver.setPrefix("/WEB-INF/views/");
            resolver.setSuffix(".jsp");
            resolver.setExposeContextBeansAsAttributes(true);
            return resolver;
        }
    ```
    > Java Config

	```XML
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"
    	p:prefix="/WEB-INF/views/"
        p:suffix=".jsp" />
    ```
    > XML Config
***

* JSTL 뷰 결정하기
	* JSP파일에서 JSTL을 사용할때 뷰를 JstlView를 사용하면
	* 스프리에서 설정한 로케일과 메시지 소스를 받아서 사용할 수 있다.
	* viewClass만 변경하면 된다.
		* resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
***

###### 6.2.2 스프링의 JSP 라이브러리 사용하기
* 스프링은 2가지 태그 라이브러리를 제공
	* 모델 속성에 바인딩 된 HTML 양식 태그를 렌더링하는 태그 라이브러리
	* 종종 유용 할 수있는 유틸리티 태그 라이브러리
***

* 폼에 모델 바인딩하기
	* 스프링 form-binding JSP 태그 라이브러리
        * 모델의 객체에 바인딩되서 모델 객체의 프로퍼티 값으로 채워질수 있다.
        * 사용자에게 에러를 전달할 수 있음
        * 사용법 - 다음 선언을 사용
            ```XML
            <@% taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
            ```
        * 14개의 태그를 제공
			* P206 표6.2

   	* 등록폼에 태그 라이브러리 적용
	```XML
    <sf:form method="POST" commandName="spitter">
    	First Name: <sf:input path="firtnName" /><br />
        Last Name: <sf:input path="lastName" /><br />
        Email: <sf:input path="email" /><br />
        Username: <sf:input path="username" /><br />
        Password: <sf:input path="password" /><br />
        <input type="submit" value="Register" />
    </sf:form>
    ```
    > path는 value로 렌더링 되면서 모델 객체에 값이 있으면 값을 표시힌다.

	* commandName에 지정한 spitter 객체를 컨트롤러에 추가
	```JAVA
    @RequestMapping(value="/register", method=GET)
    public String showRegistrationForm(Model model) {
    	model.addAttribute(new Spitter());
        return "registerForm";
    }
    ```

	* 잘못된 값 입력 후 제출했을때 보여지는 등록 페이지
	```XML
    <form id="spitter" action="/spitter/spitter/register" method="POST">
    ..... P208 예제
    </form>
    ```

	* 태그 라이브러리 내에서 HTML 5-specific text field - data, range, email 등이 선언 가능
	```JAVA
    Email: <sf:input path="email" type="email" /><br />
    -->
    Email: <input id="email" name="email" type="email" value="jack" /><br />
    ```

	* 검증이 실패하면 폼은 이전에 있었던 값으로 채워진다.
	* 검증 실패에 대한 가이드를 위해 &lt;sf:errors&gt;를 제공한다.
***

* 오류 표시하기
	* 검증오류가 발생하면 오류 내용은 모델에 담겨서 request에 담겨서 전달된다.
	* &lt;sf:errors&gt; 태그 사용
	```XML
    <sf:form method="POST" commandName="spitter">
    	First Name: <sf:input path="firstName" />
        	<sf:errors path="firstName" /><br />
    </sf:form>
    ```
    > path 애트리뷰트 - 에러를 위해서 표시되어야할 모델 객체의 속성
    > path에 해당하는 프로퍼티가 없다면 아무것도 렌더링되지 않음
    
    * 오류 강조를 위한 스타일 변경
    ```XML
    // JSP
    <sf:errors path="firstName" cssClass="error" /><br />
    
    // CSS
    span.error {
    	color: red;
    }
    ```
	
    * 오류 모아서 출력
    ```XML
        <sf:form method="POST" commandName="spitter" >
          <sf:errors path="*" element="div" cssClass="errors" />
          First Name:<sf:input path="firstName" /><br />
          Last Name: <sf:input path="lastName" /><br />
          Email: <sf:input path="email" /><br />
          Username: <sf:input path="username" /><br />
          Password: <sf:input path="password" /><br />
          <input type="submit" value="Register" />
        </sf:form>
    // style.css
        div.errors {
            background-color: #ffcccc;
            border: 2px solid red;
        }
    ```
    > path를 *로 설정해서 모든 오류를 렌더링 처리
    > 오류를 모아서 출력하기 위해 element를 div를 사용
    > 스타일에 errors 추가
    
    * 수정이 필요한 필드의 강조를 위해 label 적용
    ```XML
    	<sf:form method="POST" commandName="spitter" >
        	<sf:label path="firstName" cssErrorClass="error">First Name</sf:label>:
            <sf:input path="firstName" cssErrorClass="error" /><br />
        ...
        </sf:form>
    // style.css
    label.error {
    	color: red;
    }
    input.error {
    	background-color: #ffcccc;
    }
    ```
	> cssErrorClass - error가 발생할 경우 지정한 클래스로 렌더링 -> class="error"

	* 검증 어노테이션에 message 어트리뷰트 설정
	```JAVA
        @NotNull
        @Size(min=5, max=16, message = "{username.size}")
        private String username;

        @NotNull
        @Size(min=5, max=25, message = "{password.size}")
        private String password;

        @NotNull
        @Size(min=2, max=30, message = "{firstName.size}")
        private String firstName;

        @NotNull
        @Size(min=2, max=30, message = "{lastName.size}")
        private String lastName;

        @NotNull
        @Email(message = "{email.valid}")
        private String email;
    ```
    > {}로 묶인 값을 가진 스트링으로 대치 됨 - 값은 프로퍼티 파일에 기술

	* ValidationMessages.properties 파일
	```XML
    firstName.size=First name must be between {min} and {max} characters long.
    lastName.size=Last name must be between {min} and {max} characters long.
    username.size=Username must be between {min} and {max} characters long.
    password.size=Password must be between {min} and {max} characters long.
    email.valid=The email address must be valid.
    ```
	> locale-specific 하게도 가능하다.
	> ValidationMessages_es.properties 파일만 생성하면 됨
***

* 스프링의 일반 태그 라이브러리
	* 일반적인 JSP 태그 라이브러리도 제공
	```XML
    	<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
    ```
    > P215의 10개의 태그가 사용 가능
    > 예전에 사용되던 방식이며 스프링폼 태그로 인해 거의 사용되지 않음
***

* 다국어 메시지 표시하기
	* 텍스트를 태그를 사용하여 다국어로 렌더링 처리한다.
	```XML
    	// 일반 텍스트
        <h1>Welcome to Spittr!</h1>
        
        // 태그 사용
        <h1><s:message code="spittr.welcome" /></h1>
    ```
    > 메시지 소스에 키가 spittr.welcome인 텍스트를 렌더링한다.

	* 메시지 소스 빈 등록
	```JAVA
    	@Bean
        public MessageSource messageSource() {
        	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("messages");
            
            return messageSource;
        }
    ```
    > 렌더링이 동작하게 하기 위해서는 메시지 소스를 빈으로 등록
    > basename 프로퍼티로 클래스 루트의 메시지 파일명을 지정

	* ReloadableResourceBundleMessageSource
	```JAVA
    	@Bean
        public MessageSource messageSource() {
        	ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("file:///etc/spittr/messages");
            messageSource.setCacheSeconds(10);
            
            return messageSource;
        }
    ```
    > 재시작 없이 메시지를 다시 불러옴
    > basename 프로퍼티 위치를 클래스패스 루트나 외부 폴더 모두 지정 가능함

	* 프로퍼티 파일 생성
	```XML
    	spittr.welcome=Welcome to Spittr!
    ```
    > 다국어일 경우는 메시지 파일 뒤에 국가명을 붙인다 - messages_es.properties 형태
***

* URL 만들기
	* &lt;s:url&gt; 는 URL을 생성해서 변수로 할당하거나 response 내부에 렌더링을 한다.
	* JSTL의 &lt;c:url&gt;를 대체하며 몇가지 추가 기능이 있다.
	* 기능
		```XML
        	<a href="<s:url href="/spitter/regiser" />">Register</a>
            ==>
            <a href="/spittr/spitter/register">Register</a>
        ```
        > 컨텍스트 루트를 붙여서 렌더링 처리 한다.

		```XML
        	<s:url href="/spitter/regiser" var="registerUrl" />
            
            <a href="${registerUrl}">Register</a>
        ```
        > 나중에 템플릿에서 사용하기 위해 변수에 할당해서 사용 가능

		```XML
        	<s:url href="//spitter/register" var="registerUrl" scope="request" />
        ```
        > 기본 scope은 page scope
        > scope 속성을 설정하여 응용 프로그램, 세션 또는 요청 범위로 만들 수 있다.

		```XML
        	<s:url href="/spittles" var="spittleUrl">
           		<s:param name="max" value="60" />
                <s:param name="count" value="20" />
            </s:url>
        ```
        > 파라미터 추가는 &lt;s:param&gt;를 사용

		```XML
        	<s:url href="/spitter/{username}" var="spitterUrl">
            	<s:param name="username" value="jbauer" />
            </s:url>
        ```
        > 플레이스홀더 변수에 대응되면 변수가 있으면 사용하고 없으면 쿼리 파라미터를 변수로 사용

		```XML
        	<s:url value="/spittles" htmlEscape="true">
            	<s:param name="username" value="jbauer" />
            </s:url>
            ==>
            /spitter/spittles?max=60&amp;count=20
        ```
        > URL을 웹 페이지의 내용으로 사용하기 위한 이스케이핑 처리

		```XML
        	<s:url value="/spittles" var="spittlesJSUrl" javaScriptEscape="true">
            	<s:param name="username" value="jbauer" />
            </s:url>
            <script>
            	var spittlesUrl = "${spittlesJSUrl}"
            </script>
            ==>
            <script>
            	var spittlesUrl = "\/spitter\/spittles?max=60&count=20"
            </script>
        ```
        > URL을 자바스크립트 코드에서 사용하고 싶을때
***

* 콘텐트 이스케이핑
	* &lt;s:escapeBody&lt;
		* 모든 내용에 대해서 이스케이핑 처리를 해주는 범용 목적의 이스케이핑 태그다.
		```XML
        	<s:escapeBody htmlEscape="true">
            	<h1>Hello</h1>
            </s:escapeBody>
            ==>
            &lt;h1&gt;Hello&lt;/h1&gt;
        ```
        > 태그 사이의 내용을 이스케이핑 처리

		```XML
        	<s:escapeBody javaScriptEscape="true">
            	<h1>Hello</h1>
            </s:escapeBody>
            ==>
        ```
        > javaScript 이스케이핑 처리
***

#### 6.3 아파치 타일즈 뷰로 레이아웃 정의하기
* 모든 페이지에 적용되는 페이지 레이아웃을 정의할 수 있다.
* 스프링 MVC는 논리뷰 이름을 타일즈 정의로 해석할 수 있는 뷰 리졸버 형태를 지원한다.
***

###### 6.3.1 타일즈 뷰 리졸버 설정하기
* 몇 가지 빈을 설정해줘야 함
	* TilesConfigurer 빈
		* 타일 정의의 위치를 정하고 로드해서 타일들을 배치하는 역할
	* TilesViewResolver
		* 논리적 뷰 이름으로 타일 정의를 결정
	* Tiles2와 Tiles3에 각기 다른 패키지에 쌍으로 제공
	* 설정 - TilesConfigurer 빈 추가
	```JAVA
    	@Bean
        public TilesConfigurer tilesConfigurer() {
        	TilesConfigurer tiles = new TilesConfigurer();
            tiles.setDefinitions(new String[] {
            	"/WEB-INF/layout/tiles.xml"
            });
            tiles.setCheckRefresh(true);
            return tiles;
        }
    ```
    
    * 다수의 설정 정의
    ```JAVA
    	tiles.setDefinitions(new String[] {
        	"/WEB-INF/**/tiles.xml"
        });
    ```
    
    * TilesViewResolver 설정
    ```JAVA
        @Bean
        public ViewResolver viewResolver() {
            return new TilesViewResolver();
        }
    ```
    
    * XML 타일즈 설정 - P223
***

* 타일 정의하기
	* P224 코드6.2 Spittr 애플리케이션의 타일 정의
	```XML
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE tiles-definitions PUBLIC
            "-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN"
            "http://tiles.apache.org/dtds/tiles-config_3_0.dtd">
    <tiles-definitions>

        <definition name="base" template="/WEB-INF/layout/page.jsp">
            <put-attribute name="header" value="/WEB-INF/layout/header.jsp" />
            <put-attribute name="footer" value="/WEB-INF/layout/footer.jsp" />
        </definition>

        <definition name="home" extends="base">
            <put-attribute name="body" value="/WEB-INF/views/home.jsp" />
        </definition>

        <definition name="registerForm" extends="base">
            <put-attribute name="body" value="/WEB-INF/views/registerForm.jsp" />
        </definition>

        <definition name="profile" extends="base">
            <put-attribute name="body" value="/WEB-INF/views/profile.jsp" />
        </definition>

        <definition name="spittles" extends="base">
            <put-attribute name="body" value="/WEB-INF/views/spittles.jsp" />
        </definition>

        <definition name="spittle" extends="base">
            <put-attribute name="body" value="/WEB-INF/views/spittle.jsp" />
        </definition>

    </tiles-definitions>
    ```
    > 각 definition 요소는 최종적으로 JSP 템플릿을 참조하는 타일을 정의
    > base 타일을 상속받아 확장하는 것도 가능하다.

	* 기본 레이아웃인 page.jsp
	```XML
    <%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
    <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
    <%@ page session="false" %>
    <html>
      <head>
        <title>Spittr</title>
        <link rel="stylesheet" 
              type="text/css" 
              href="<s:url value="/resources/style.css" />" >
      </head>
      <body>
        <div id="header">
          <t:insertAttribute name="header" />
        </div>
        <div id="content">
          <t:insertAttribute name="body" />
        </div>
        <div id="footer">
          <t:insertAttribute name="footer" />
        </div>
      </body>
    </html>
    ```
    > P226 그림과 같은 레이아웃을 정의한다.
***

* header.jsp
	```XML
    <%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
    <a href="<s:url value="/" />">
    	<img src="<s:url value="/resources" />/images/spitter_logo_50.png" border="0"/>
    </a>
    ```
    
* footer.jsp
    ```XML
    Copyright &copy; Craig Walls
    ```
***

* home.jsp
	```XML
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page session="false" %>
    <h1>Welcome to Spitter</h1>

    <a href="<c:url value="/spittles" />">Spittles</a> | 
    <a href="<c:url value="/spitter/register" />">Register</a>
    ```
    > 공통요소인 page, header, footer가 빠져 있는 형태임
***

#### 6.4 Thymeleaf로 작업하기
* JSP의 문제점
	* HTML 또는 XML 형태지만, 사실은 어느쪽도 아니여서 well-formed가 아님
	* 예 - HTML 파라미터의 값으로 사용될 수 있는 JSP 태그
		```XML
        	<input type="text" value="<c:out value="${thing.name}"/>" />
        ```
        
    * JSP의 스펙이 서블릿 스펙과 강하게 결합되어 있다.
    	* 일반적인 목적의 템플릿이나 서블릿 기반이 아닌 웹 애플리케이션에는 적절하지 않다.
* Thymeleaf
	* JSP를 대체하기 위한 시도
	* 형태가 자연스럽고 태그라이브러리에 의존하지 않는다.
	* HTML이 사용가능한 곳에서 쉽게 편집 및 렌더링이 가능하다.
***

###### 6.4.1 Thymeleaf 뷰 리졸버 설정하기
* Thymeleaf-Spring 통합을 위한 빈 설정
	* Thymeleaf ViewResolver 빈 - 논리적 뷰 이름으로 Thymeleaf 템플릿 뷰를 결정한다.
	* SpringTemplateEngine 빈 - 템플릿을 처리하고 결과를 렌더링한다.
	* TemplateResolver 빈 - Thymeleaf 템플릿을 불러온다
* 자바 설정
	* http://www.thymeleaf.org/doc/articles/thymeleaf3migration.html 참고
    ```JAVA
        @Bean
        public ViewResolver viewResolver() {
            ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
            viewResolver.setTemplateEngine(templateEngine());
            viewResolver.setCharacterEncoding("UTF-8");
            return viewResolver;
        }
        @Bean
        public SpringTemplateEngine templateEngine() {
            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
            templateEngine.setEnableSpringELCompiler(true);
            templateEngine.setTemplateResolver(templateResolver());
            return templateEngine;
        }

        @Bean
        public ITemplateResolver templateResolver() {
            SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

            templateResolver.setPrefix("/WEB-INF/views/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode("HTML5");
            return templateResolver;
        }
	```
    > 뷰리졸버, 템플릿엔진, 템플릿 리졸버 설정

* XML 설정 - P229 참고
* 최종적인 뷰는 Thymeleaf 템플릿이 됨
***

###### 6.4.2 Themeleaf 템플릿 정의하기
* 템플릿은 기본적으로 HTML 파일이며 네임스페이스에 어트리뷰트를 추가해 주는 것으로 동작한다.
    ```XML
        <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
        <head>
            <title>Spitter</title>
            <link rel="stylesheet"
                  type="text/css"
                  th:href="@{/resources/style.css}"></link>
        </head>
        <body>
            <h1>Welcome to Spitter</h1>

            <a th:href="@{/spittles}">Spittles</a> |
            <a th:href="@{/spitter/register}">Register</a>
        </body>
        </html>
    ```
    > th:href를 제외하고는 기본적인 HTML 파일과 같다.
    > JSP와 달리 특별한 처리 과정 없이 자연스럽게 렌더링하거나 편집이 가능하다.
    > Thymeleaf 없이 렌더링된 모습 - P231 그림6-6
***

* Thymeleaf로 폼 바인딩하기
	* 스프링의 폼 바인딩 태그 라이브러리
		```XML
        	<sf:label path="firstName" cssErrorClass="error">First Name</sf:label>
            <sf:input path="firstName" cssErrorClasss="error" /><br />
        ```
		* 값을 매핑해주고, 에러 발생시 cssErrorClass 등을 지원한다.
	* 스프링의 JSP 태그로 수행한 폼 바인딩 대신 Thymeleaf의 스프링 전용 방법
		```XML
        	<label th:class="#{#fields.hasErrors('firstName')}? 'error'"> First Name</label>:
            <input type="text" th:field="*{firstName}" th:class="${#fields.hasErrors('firstName')}? 'error'" /><br/>
        ```
        > input태그는 보조객체로부터 firstName 필드를 참조하기 위해 th:field 애트리뷰트를 사용한다.
        > 보통은 value 애트리뷰트를 사용하지만 여기서는 필드에 바인딩하기 위해 사용되었다.

	* 등록 폼 템플릿 - 데이터 바인딩 측면에서 살펴보자
	```XML
    	P233 예제
    ```
    > 바인딩을 위해 Thymeleaf 애트리뷰트와 *{} 표현식을 사용함
    > 오류 여부 확인을 위해 th:if 애트리뷰트 사용
    > ${}는 OGNL이며, 스프링에서는 SpEL이다. ${spitter}는 키가 spitter인 model 프로퍼티를 참조한다.
    > *{}는 선택 표현식으로 선택된 객체에 대해서만 처리한다. *{firstName} 표현식은 Spitter 객체의 firstName 프로퍼티를 검증한다.
***

#### 6.5 요약
* 스프링은 뷰 렌더링에 JSP부터 타일즈까지 다양한 선택을 제공한다.
* 뷰와 뷰 레졸루션 옵션을 살펴보았고 JSP와 타일즈 사용법을 살펴 보았다.
* JSP의 대안으로 Thymeleaf 사용법을 살펴 보았다.
***

## 8장 스프링 웹 플로로 작업하기
* 다룰내용
	* 대화형 웹 애플리케이션 만들기
	* 상태 및 동작의 플로 정의하기
	* 웹 플로 보안
***

#### 8.1 스프링에 웹 플로 설정하기
* 웹 플로는 MVC 기반
	* 플로에 전달되는 모든 요청들은 DispatcherServlet을 통한다.
	* flow 요청을 처리하고 flow를 실행하기 위해서는 스프링 컨텍스트에 몇가지 빈을 설정해야 한다.
	* 스프링 웹 플로는 현재는 XML만 지원한다.
		* P268 XML 네임스페이스 선언
***

###### 8.1.1 플로 실행기 연결

```XML
<flow:flow-executor id="flowExecutor" />
```
> 플로 실행기는 플로를 생성하고 실행하는 역할을 수행한다.ㅡㅁ

***

###### 8.1.2 플로 설정하기
* 플로 정의를 불러와서 플로 실행기에서 사용이 가능하도록 한다.
    ```XML
    <flow:flow-registry id="flowRegistry" base-path="/WEB-INF/flows">
        <flow:flow-location-pattern value="*-flow.xml" />
    </flow:flow-registry>
    ```
    > -flow.xml로 끝나는 모든 XML 파일을 플로 정의 파일로 간주
***

###### 8.1.3 플로 요청 처리하기
* DispatcherServlet이 플로 요청을 스플링 웹 플로어로 보내기 위해 FlowHandlerMapping이 필요
```XML
	<bean class="org.springframework.webflow.mvc.servlet.FlowHandlerMapping">
	    <property name="flowRegistry" ref="flowRegistry" />
    </bean>
```
> 스프링 애플리케이션 컨텍스트에 설정

* FlowHandlerAdapter의 역할
	* 스프링 MVC 컨트롤러의 흐름과 프로세스로 오는 요청들을 처리
	```XML
    	<bean class="org.springframework.webflow.mvc.servlet.FlowHandlerAdapter">
        	<property name="flowExecutor" ref="flowExecutor" />
        </bean>
    ```
    > DispatcherServlet과 스프링 웹 플로를 연결시키는 역할을 한다.
***

#### 8.2 플로의 컴포넌트
