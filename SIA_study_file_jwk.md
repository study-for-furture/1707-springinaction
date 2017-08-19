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