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
* 스프링에서는 컨테이너가 협엽할 객체에 대한 레퍼런스를 준다.
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




















