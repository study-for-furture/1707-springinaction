# 3. 고급 와이어링

## 3.1 환경과 프로파일
* 소프트웨어 개발 시 가장 어려운 일 중 하나는 어떤 환경에서 다른 환경으로 **마이그레이션(Migration)**하는 것이다.
* 마이그레이션의 예로는 DB설정, 암호화 알고르짐 및 외부 시스템과의 통합이 있다.
* 예를 들어 DB 구성을 보면 테스트 데이터를 미리 로드한 내장 DB를 사용할 가능성이 높으며 @Bean 메소드로 EmbeddedDatabaseBuilder를 사용한다.

```JAVA
@Bean(destroyMethod="shutdown")
public DataSource dataSource() {
	return new EmbeddedDatabaseBuilder()
    	.addScript("classpath:schema.sql")
       	.addScript("classpath:test-data.sql")
        .build();
}
```

* 위 유형은 javax.sql.DataSource의 빈을 작성한다.
* EmbaddedDatabaseBuilder를 사용해 임베디드 Hypersonic DB를 셋업하며, 스키마는 Schema.sql, 테스트 데이터는 test-data.sql에서 가져온다.
* 개발을 위해 EmbeddedDatabaseBuilder에서 만든 DataSource가 완벽하더라도 생산에선 좋지 않은 선택일 수 있다.
* 생산 환경에선 JNDI를 사용해 컨테이너에서 DataSource를 얻는다.

```JAVA
@Bean
public DataSource dataSource() {
	JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
    jndiObjectFactoryBean.setJndiName("jdbc/myDS");
    jdniObjectFactoryBean.setResourceRef(true);
    jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
    return (DataSource) jndiObjectFactoryBean.getObject();
}
```

* JNDI에서 DataSource를 얻으면 컨테이너 관리 연결 풀에서 DAtaSource 핸드 오프(hand off)를 가지는 방식으로 컨테이너를 사용해 의사결정을 한다.
* JNDI에서 관리 데이터소스를 사용하면 생산엔 적합하지만 통합테스트나 개발자 테스트에는 불필요하며 복잡하다.
* QA환경에선 완전히 다른 DataSource 설정을 선택할 수 있고 다음처럼 가공 DBCP연결 풀 구성을 선택할 수 있다.

```JAVA
@Bean(destroyMethod="close")
public DataSource dataSource() {
	BasicDataSource dataSource = new BasicDataSource();
    dataSource.setUrl("jdbc:h2:tcp://dbserver/~/test");
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUsername("sa");
    dataSource.setPassword("password");
    dataSource.setInitialSize(20);
    dataSource.setMaxActive(30);
    return dataSource;
}
```

* 위 dataSource() 메소드의 세 개 버전은 모두 다르다.
* javax.sql.DataSource인 것이 유일한 공통점이다.
* 환경에 따라 가장 적절한 설정을 선택하도록 DataSource 빈 설정 방법을 찾아야 한다.

### 3.1.1 빈 프로파일 설정하기
* 스프링 버전 3.1에선 빈 프로파일을 도입했다.
* 프로파일을 사용하려면 하나 이상의 프로파일에 모든 빈 정의를 수집해 응용 프로그램이 각 환경에 배포될 때 적절한 프로파일이 활성화되어야 한다.
* Java 구성에선 빈이 속한 프로파일을 지정하는 @Profile 주석을 사용한다.
* 임베디드 DB의 DataSource 빈은 이러한 설정 클래스로 구성된다.

```JAVA
@Configuration
@Profile("dev")
public class DevelopmentProfileConfig {
	@Bean(destroyMethod="shutdown")
    public DataSource dataSource() {
    	return new EmbeddedDatabaseBuilder()
        	.setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:schema.sql")
            .addScript("classpath:test-data.sql")
            .build();
    }
}
```

* 주의할 점은 클래스 수준에 적용되는 @Profile 애너테이션이다.
* 이는 설정 클래스의 빈이 dev 프로파일이 활성화된 경우에만 작성되어야 함을 스프링에 알려준다.
* dev 프로파일이 활성화되지 않은 경우 @Bean 메소드는 무시된다.
* 다음 코드는 다른 설정 클래스를 가진 릴리즈 용 코드다.

```JAVA
@Configuration
@Profile("prod")
public class ProduectionProfileConfig {
	@Bean
    public DataSource dataSource() {
    	JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/myDS");
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
        return (DataSource) jndiObjectFactoryBean.getObject();
    }
}
```

* 이 경우 prod프로파일 활성화 시 생성된다.
* 스프링 3.1은 클래스 수준에서 @Profile 애너테이션을 사용하지만 3.2에선 @Bean애너테이션과 함께 메소드 수준에서 @Profile을 사용한다.

```JAVA
@Configuration
public class DataSourceConfig {
	@Bean(destroyMethod="shutdown")
    @Profile("dev")
    public DataSource embeddedDataSource() {
    	return new EmbeddedDatabaseBuilder()
        	.setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:schema.sql")
            .addScript("classpath:test-data.sql")
            .build();
    }
    
    @Bean(destroyMethod="shutdown")
    @Profile("prod")
    public DataSource jndiDataSource() {
    	JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/myDS");
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
        return (DataSource) jndiObjectFactoryBean.getObject();
    }
}
```

* 여기서 명확하지 않은 것은 각 프로파일에 따라 빈이 활성화되지만 정의되지 않은 다른 빈이 있으며, 프로파일 활성화 여부와 상관 없이 지정되지 않은 모든 빈은 항상 활성화가 된다.

##### XML로 프로파일 설정하기
* ```<beans>``` 요소의 profile 애트리뷰트를 설정해 XML로 프로파일된 빈을 설정할 수 있다.
* 임베디드 DB의 DataSource 빈을 정의하려면 다음과 같다.

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\그림1.png)

* 모든 설정 XML 파일은 배치 유닛(WAR파일)에 수집되지만, 오직 profile 애트리뷰트는 사용될 활성 프로파일에 매칭된다.
* 루트 ```<beans>``` 요소에 포함된 ```<beans>``` 요소를 정의하는 옵션을 사용한다.
* 하나의 XML 파일에 모든 프로파일 빈 정의를 수집하는데 도움이 된다.

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\그림2.png)

### 3.1.2 프로파일 활성화하기
* 스프링은 프로파일 활성 상태를 확인하는 **spring.profile.active**와 **spring.profile.default** 프로퍼티를 가진다.
* active가 설정되면 프로파일이 활성 상태인지를 결정하며 설정되어 있지 않으면 default가 된다.
* active나 default가 설정되어 있지 않은 경우 프로파일에 정의되지 않은 빈만 만들어진다.
* 프로퍼티 설정하기 위한 여러 가지 방법
	* DispatcherServlet에 초기화된 파라미터
	* 웹 애플리케이션의 컨텍스트 파라미터
	* JNDI 엔트리
	* 환경변수
	* JVM 시스템 프로퍼티
	* 통합테스트 클래스에서 @ActiveProfiles 애너테이션 사용

* 다음은 웹 애플리케이션의 web.xml파일에 spring.prfile.default를 설정하는 예제이다.

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\그림3.png)

* 이처럼 default를 설정하면 모든 개발자가 추가 설정 없이 개발 설정을 사용하여 실행한다.
* 애플리케이션은 배포될 때 시스템 프로퍼티나 환경변수 또는 JNDI를 사용해 spring.profiles.active를 제대로 설정할 수 있다.
* 기본적으로 default보단 active의 프로파일 설정이 우선이다.

##### 프로파일 테스팅
* 스프링은 테스트를 실행할 때 활성화 될 필요가 있는 프로파일을 지정할 수 있도록 @ActiveProfiles 애너테이션을 제공한다.
* dev프로파일 활성화 예제

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\그림4.png)

## 3.2 조건부 빈
* 스프링 4.0에선 @Bean을 적용할 수 있는 새로운 @Conditional 애너테이션이 추가되었다.
* 이는 소정의 조건이 참일 경우 빈을 생성하고 그렇지 않음 무시한다.
* 예제 p.116(책 p.89)
* Condition 인터페이스 내 matches()메소드가 true를 돌려줄 경우 @Conditional으로 애너테이션 된 빈이 생성된다. 반면 false가 반환되면 빈은 작성되지 않는다.
* 예제 p.116 - 코드 3.5 Conditional에 magic 존재 여부 체크
* 환경 프로퍼티의 magic이란 이름이 존재하는지 확인하기 위해 ConditionContext객체의 Environment를 사용한다.
* matches메소드에서 true일 경우 @Conditional 애너테이션의 빈이 생성되고 false일 경우 무시된다.
* matches메소드엔 의사결정 시 사용할 ConditionContext와 AnnotatedTypeMetadata가 주어진다.
* ConditionContext는 다음을 수행할 수 있다.
	* getRegistry()반환을 통한 BeanDefinitionRegistry로 빈 정의 확인
	* 빈의 존재를 확인하고 getBeanFactory()에서 반환되는 ConfigurableListableBeanFactory를 통해 빈 프로퍼티를 발굴
	* getEnvironment()로부터 얻은 Environment를 통해 환경 변수 값 확인
	* getResourceLoder()에서 반환된 ResourceLoader를 통해 로드된 자원 내용을 읽고 검사.
	* getClassLoader()에서 반환된 ClassLoader를 통해 클래스의 존재를 로드하고 확인한다.

* AnnotatedTypeMetadata는 @Bean메소드의 애너테이션을 검사할 수 있는 기회를 제공한다.
* AnnotatedTypeMetadata의 인터페이스

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\그림5.png)

* isAnnotated메소드를 사용해 @Bean메소드가 특정 애너테이션 타입을 사용해 애너테이션을 붙일 수 있는지 검사한다.
* @Profile애너테이션은 @Conditional을 사용해 애너테이션되고 Condition의 구현으로 Profile Condition을 참조한다.

## 3.3 오토와이어링의 모호성
* 정확히 하나의 빈이 원하는 결과와 일치할 때 오토와이어링은 동작한다.
* 일치하는 빈이 여럿이 있으면 모호함 때문에 스프링의 프로퍼티, 생성자인수, 메소드 파라미터의 오토와이어링은 어렵다.
* 오토 와이어링의 모호성을 나타내기 위해 @Autowired를 가지는 setDessert() 메소드가 애너테이션된다고 가정한다.

```JAVA
@Autowired
public void setDessert(Dessert dessert) {
	this.dessert = dessert;
}
```

* 이 Dessert는 인터페이스이며 세 개의 클래스에서 구현된다.

```JAVA
@Component
public class Cake implements Dessert {...}

@Component
public class Cookies implements Dessert {...}

@Component
public class IceCream implements Dessert {...}
```

* @Component로 애너테이션되어 컴포넌트 스캐닝으로 찾을 수 있고 컨텍스트에서 빈으로 생성된다.
* 이 경우 setDessert()에서 Dessert 파라미터를 오토와이어링할때 명확하지가 않아 스프링은 예외를 발생시킨다.
* 스프링은 단일후보로 선택을 좁히기 위해 한정자를 사용한다.

### 3.3.1 기본 빈 지정
* 빈 선언 시, 기본 빈으로 후보 빈 중 하나를 지정해 오토와이어링의 모호함을 피할 수 있다.
* @Primary는 컴포넌트 스캐닝된 빈을 위한 @Component와 자바 설정에서 선언된 빈의 @Bean을 함께 사용한다.

```JAVA
@Component
@Primary
public class IceCream implements Dessert {...}
```

* 또는 자바 성정으로 명시적으로 빈을 지정한다.

```JAVA
@Bean
@Primary
public Dessert iceCream() {
	return new IceCream();
}
```

* XML로도 기본 빈을 지정할 수 있다.
* ``<bean>`` 요소는 기본 빈을 지정하기 위해 primary 애트리뷰트를 가진다.

```XML
<bean id = "iceCream"
	class="com.desserteater.IceCream"
    primary="true"/>
```

### 3.3.2 오토와이어링 빈의 자격
* 기본 빈의 한계점은 @Primary가 하나의 명백한 옵션 선택을 하지 못한다는 점이다.
* @Qualifier 애너테이션은 주입 대상 빈을 지정할 주입 지점에서 @Autowired나 @Inject와 함께 적용된다.

```JAVA
@Autowired
@Qualifier("iceCream")
public void setDessert(Dessert dessert) {
	this.dessert = dessert;
}
```

* @Qualifier의 파라밑터는 주입할 빈의 ID이다.
* @Qualifier("iceCream")은 수식으로 문자열 "iceCream을 가지는 빈을 참조한다.
* 다른 수식자를 지정하지 못하는 경우 모든 빈은 그들의 빈 ID와 같은 기본 수식자를 부여받는다.

##### 맞춤형 수식자 만들기
* 수식자로 빈 ID에 의존하는 대신 빈에 자신의 수식자를 지정한다.
* 빈 선언에서 @Qualifier 애너테이션을 배치하는 것이다.

```JAVA
@Component
@Qualifier("cold")
public class IceCream implements Dessert {...}
```

* 위 경우 cold라는 수식자는 IceCream 빈에 할당된다.
* 주입 지점에서 cold 수식자를 참조하는 한 문제는 발생하지 않는다.

```JAVA
@Autowired
@Qualifier("cold")
public void setDessert(Dessert dessert) {
	this.dessert = dessert;
}
```

* 명시적으로 자바 설정을 가지고 빈을 정의할 때 @Qualifier를 @Bean 애너테이션과 함께 사용한다.

```JAVA
@Bean
@Qualifier("cold")
public Dessert iceCream() {
	return new IceCream();
}
```

* @Qualifer 값을 정의하는 경우 빈의 특성 또는 서술적 용어를 사용하는 것이 좋다.

##### 맞춤형 수식자 애너테이션 정의하기
* 예를 들어 새로운 Dessert 빈을 도입해본다.

```JAVA
@Component
@Qualifier("cold")
public class Popsicle implements Dessert {...}
```

* 두 개의 "cold" 디저트가 생성되고 오토와이어링의 모호함에 직면한다.
* 모호함을 해결하려면 빈 정의에 다른 @Qualifier를 고정시키는 것이다.

```JAVA
@Component
@Qualifier("cold")
@Qualifier("creamy")
public class IceCream implements Dessert {...}

@Component
@Qualifier("cold")
@Qualifier("fruity")
public class Popsicle implements Dessert {...}
```

* 주입 시엔 다음과 같이 IceCream으로 빈 후보를 줄인다.

```JAVA
@Autowired
@Qualifier("cold")
@Qualifier("creamy")
public void setDessert(Dessert dessert) {
	this.dessert = dessert;
}
```

* 여기엔 문제점이 있는데 자바는 동일한 유형의 여러 애너테이션이 같은 항목에 반복될 수 없다.
* 하지만 빈에 자격이 주어지고 특성을 표현하기 위해 사용자 지정 수식자 애너테이션을 작성한다.
* @Qualifier를 작성하는 것이 아니라 다음과 같이 맞춤형 애너테이션을 작성한다.

```JAVA
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD,
	ElementType.METHOD, ElemehtType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Cold { }

// @Retention : 어노테이션의 범위로 어떤 지점까지 영향을 미치는가를 결정한다.
// @Target : 어노테이션이 적용될 위치를 결정
// 기타 어노테이션에 대한 설명 (http://jdm.kr/blog/216)
```

* 사용자 지정 수식자 애너테이션을 정의해 자바 컴파일러에서 제한없이 여러 개의 수식자를 사용할 수 있다.
* @Qualifier를 사용해 문자열로 수식자를 지정하는 것 보다 더 타입세이프하다.

## 3.4 빈의 범위
* 기본적으로 스프링에서 생성되는 모든 빈은 싱글톤이다.
* 스프링은 빈이 생성될 수 있는 여러 개의 범위를 정의한다.
	* 싱글톤 : 전체 애플리케이션을 위해 생성되는 빈의 인스턴스 하나
	* 프로토타입 : 빈이 주입될 때마다 생성되거나 스프링 애플리케이션 컨텍스트에서 얻는 빈의 인스턴스 하나
	* 세션 : 웹 애플리케이션에서 각 세션용으로 생성되는 빈의 인스턴스 하나
	* 요청 : 웹 애플리케이션에서 각 요청용으로 생성되는 빈의 인스턴스 하나
* 싱글톤 타입이 기본이나 어떤 상태를 유지하지만 재사용용으로는 안전하지 않은 이변성 클래스 타입에는 적합하지 않다.
* @Component 또는 @Bean 애너테이션과 관련된 다른 타입을 선택하기 위해 @Scope애너테이션을 사용한다.
* 빈을 찾고 선언하기 위해 컴포넌트 스캐닝을 한다면, 프로토타입 빈을 만들기 위해 @Scope를 사용해 bean 클래스를 애너테이션 한다.

```JAVA
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Notepad{...}
```

* ConfigurableBeanFactory는 상수 SCOPE_PROTOTYPE을 사용해 스포토타입 범위를 지정한다.
* 프로토타입으로써 Notepad 빈을 설정한다면 원하는 범위를 지정하기 위해 @Bean과 @Scope를 사용한다.

```JAVA
@Bean
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public Notepad notepad() {
	return new Notepad();
}
```

* XML로 빈을 설정할 때 bean 요소의 scope 애트리뷰트를 사용해 설정한다.

```XML
<bean id="notepad"
	class="com.myapp.Notepad"
    scope="prototype" />
```

### 3.4.1 요청과 세션 범위 작업하기
* 장바구니에 관련된 빈은 사용자에게 각각 주어지므로 세션 범위의 빔이 가장 알맞다.
* 세션 범위를 적용하려면 @Scope 애너테이션을 사용한다.

```JAVA
@Component
@Scope(
	value=WebApplicationContext.SCOPE_SESSION,
    proxyMode=ScopedProxyMode.INTERFACES)
public ShoppingCart cart() {...}
```

* WebApplicationContext에서 SCOPE_SESSION 상수를 value로 지정한다.
* 웹 애플리케이션의 각 세션당 ShoppingCart 빈의 인스턴스를 생성하도록 스프링에 지시한다.
* ShoppingCart 빈은 주어진 세션을 위해 생성되고 해당 세션에 관해선 본질적으로 싱글톤이다.
* @Scope도 ScopedProxyMode.INTERFACES로 설정되는 proxyMode 특성을 가진다.
* 싱글톤 범위의 빈에 세션 범위 또는 요청 범위 빈을 주입할 때 발생하는 문제를 해결한다.
* 하지만 proxyMode는 문제점이 있다.

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

* 스프링 애플리케이션 컨텍스트가 로드될 수 있도록 StoreService 싱글톤 빈이 생성된다.
* 스프링은 이제 setShoppingCart()로 ShoppingCart를 주입한다.
* 그러나 세션 범위를 가지는 ShoppingCart는 존재하지 않으며 사용자와 함께 세션이 만들어질 때까지 ShoppingCart 인스턴스는 존재하지 않는다.
* StoreService 장바구니에서 작업할 필요가 있을 때 어떤 세션이 수행하는지 알기 위해 ShoppingCart인스턴스를 작동하기 위한 StoreService가 필요하다.
* ShoppingCart 빈을 StoreService에 주입하는 대신 스프링은 ShoppingCart 빈에 프록시를 주입한다.
* StoreService가 ShoppingCart의 메소드를 호출할 때 프록시가 호출된 메소드를 처리할 것이며 실제 세션 범위 ShoppingCart 빈에 대한 호출을 위임한다.
* proxyMode 애트리뷰트는 ShoppingCart의 인터페이스를 구현하고 구현 빈에 위임할 필요가 있다는 ScopedProxyMode.INTERFACES로 설정된다.
* ShoppingCart가 추상클래스인 경우 스프링은 인터페이스 기반 프록시를 만들 수 있다.
* 빈의 타입이 구체적인 클래스인 경우, 프록시가 타킷 글래스 확장으로 생성되어야 함을 나타내기 위해 ScopedProxyMode.TARGET_CLASS에 proxyMode를 설정한다.

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\그림3.1.png)

### 3.4.2 XML로 범위 프록시 선언하기
* XML로 세선 또는 요청 범위의 빈을 선언하는 경우 @Scope 또는 proxyMode애트리뷰트를 사용할 수 없다.
* bean요소의 scope 애트리뷰트를 사용하여 빈의 범위를 설정한다.
* 프록시 모드를 설정하려면 스프링의 AOP 네임스페이스에서 새로운 요소를 사용해야만 한다.

```XML
<bean id="cart"
	class="com.myapp.ShoppingCart"
    scope="session">
  <aop:scoped-proxy />
</bean>
```

* <aop:scoped-proxy>는 @Scope 애너테이션의 proxyMode 애트리뷰트에 대한 스프링 XML의 대응이다.
* 기본적으로 타깃 클래스 프록시를 생성하기 위해 CGLIB를 사용한다.
* proxy-target-class 애트리뷰트를 false로 설정해 인터페이스 기반 프록시를 생성할 수 있다.

```XML
<bean id="cart"
	class="com.myapp.ShoppingCart"
    scope="session">
  <aop:scoped-proxy proxy-target-class="false" />
</bean>
```

* <aop:scoped-proxy> 요소를 사용하기 위해 XML설정에서 스프링 aop 네임스페이스를 선언한다.

## 3.5 런타임 값 주입
* 빈 와이어링의 또 다른 측면은 빈 프로퍼티나 인수로 해당 생성자에 값을 연결할 때 볼 수 있다.

```JAVA
@Bean
public CompactDisc setPeppers() {
	return new BlackDisc(
    	"Sgt. Pepper's Lonely Hearts Club Band",
        "The Beatles");
    )
}
```

* BlankDisc 빈을 위한 제목, 아티스트를 설정했더라도 값은 설정 클래스에 하드코딩된다.
* XML에서 이렇게 수행했다면 마찬가지로 값은 하드코딩 될 것이다.

```XML
<bean id="sgtPeppers"
	class="soundsystem.BlackDisc"
    c:_title="Sgt. Pepper's Lonely Hearts Club Band"
    c:_artist="The Beatles" />
```

* 하드코딩 된 경우 런타입에 값을 평가하는 두 가지 방법을 제공한다.
	* 프로퍼티 플레이스 홀더
	* 스프링 표현 언어

### 3.5.1 외부 값 주입
* 스프링에서 외부 값을 처리하기 위한 가장 간단한 방법은 프로퍼티 소스 선언과 스프링 환경을 통한 프로퍼티 검색이다.
* BlankDisc 빈을 와이어링 하기 위해 외부 프로퍼티를 사용하는 기본 스프링 설정 클래스 예제이다.
* 예제 p.133
* 위 예제에서 @PropertySource는 클래스패스의 app.prperties를 참조한다.

```XML
disc.title = Sgt. Pepper's Lonely Hearts Club Band
disc.artist = The Beatles
```

* 이 파일은 스프링 환경으로 로딩되고 추후에 검색된다.
* disc() 메소드에서 새 BlankDisc가 생성되고 생성 인자들은 getProperty()를 호출해 프로퍼티 파일을 읽는다.

##### 스프링 환경에 대해 더 살펴보기
* 위에서 본 getProperty() 메소드가 프로퍼티 값을 가져오기 위해 사용할 수 있는 유일한 메소드는 아니다.
* getProperty()는 네 가지 변형으로 오버로드된다.
	* String getProperty(String key)
	* String getProperty(String key, String defaultValue)
	* T getProperty(String key, Class<T> type)
	* T getProperty(String key, Class<T> type, T defaultValue)

* 처음과 두번째는 항상 문자열 값을 반환한다.

```JAVA
@Bean
public BlankDisc disc() {
	return new BlankDisc(
    	env.getProperty("disc.title", "Rattle and Hum"),
        env.getProperty("disc.artist", "U2"));
}
```

* 아래의 두 가지 형식은 위 처럼 모든 값이 String이 아닌 것을 볼 수 있다.
* 연결 풀 유지 관련 예제를 가정해보면 프로퍼티 파일에서 String 값을 찾으면 사용 전 Integer로 변환할 필요가 있다.
* 변환 시에는 오버로드된 getProperty() 메소드 중 하나를 사용한다.

```JAVA
int connectionCount = env.getProperty("db.connection.count", Integer.class, 30);
```

* 기본 값으로 지정되지 않은 getProperty() 메소드 중 하나를 사용하거나 프로퍼티가 정의되지 않는다면 널을 가질 수 있다.
* 프로퍼티 정의가 필요하면 다음과 같이 getRequiredProperty()를 사용한다.

```JAVA
@Bean
public BlankDisc disc() {
	return new BlankDisc(
    	env.getRequiredProperty("disc.title"),
        env.getRequiredProperty("disc.artiest"));
}
```

* 프로퍼티가 정의되지 않으면 IllegalSTateException이 발생한다.
* 프로퍼티 존재를 확인해야 할 경우 Environment에서 containsProperty()를 호출한다.

```JAVA
boolean titleExists = env.containsProperty("disc.title");
```

* 프로퍼티를 Class로 처리해야 한다면 getPropertyAsClass() 메소드를 사용한다.

```JAVA
Class<CompactDisc> cdClass = env.getPropertyAsClass("disc.class", CompactDisc.class);
```

* Environment는 프로파일이 활성화되어 있는지 확인하는 몇 가지 메소드를 제공한다.
	* String[] getActiveProfiles() : 활성화된 프로파일 명 배열을 반환
	* String[] getDefaultProfiles() : 기본 프로파일 명 배열을 반환
	* boolean acceptProfiles(String... profiles) : 환경에 주어진 프로파일을 지원하면 참을 반환

##### 프로퍼티 플레이스홀더 처리하기
* 스프링은 프로퍼티 파일로 프로퍼티를 내보내는 옵션을 지원하며 플레이스 홀더 값을 사용하여 스프링 빈으로 플로그인한다.
* 스프링 와이어링에서 플레이스홀더는 ${...}로 쌓여진 프로퍼티 명이다.
* XML로 BlankDisc에 대한 생성자 인자 처리 예제이다.

```XML
<bean id = "sgtPeppers"
	class="soundsystem.BlankDisc"
    c:_title="${disc.title}"
    c:_artist="${disc.artist}" />
```

* 이를 통해 XML에 하드코딩된 값을 사용하지 않고, 외부 소스로부터 연결해 입력이 가능하다.
* 컴포넌트를 초기화히기 위한 컴포넌트 스캐닝과 오토와이어링에서 플레이스홀더를 지정할 수 있는 설정 파일과 클래스는 없다.
* @Autowired 애너테이션을 사용할 때와 동일한 방법으로 @Value 애너테이션을 사용한다.

```JAVA
public BlankDisc(
	@Value("{disc.title}") String title,
    @Value("{disc.artist"}) String artist) {
	this.title=title;
    this.artist=artist
}
```

* 플레이스홀더 값을 사용하기 위해 PropertyPlaceholderConfigurer 빈 또는 PropertySourcesPlaceholderconfigurer 빈을 설정한다.
* 스프링 3.1에선 스프링Environment와 프로퍼티 소스 세트에 대한 플레이스홀더를 처리하므로 PropertySourcesPlaceholderConfigurer가 더 선호된다.
* 다음 @Bean 메소드는 자바 설정에서 PropertySourcesPlaceholderConfigurer를 설정한다.

```JAVA
@Bean
public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
	return new PropertySourcesPlaceholderConfigurer();
}
```

* XML 설정을 사용한다면 스프링 컨텍스트 네임플레이스의 ```<context:property-placeholder>```요소는 PropertySourcesPlaceholderConfigurer 빈을 제공한다.

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\그림3-2.png)

### 3.5.2 스프링 표현식 와이어링
* 스프링 표현 언어(SpEL, Spring Expression Language) : 런타임에 평가하는 표현식을 이용해 빈의 프로퍼티나 생성자 인자에 값을 할당하는 언어
* SpEL에선 많은 트릭을 사용할 수 있다.
	* ID로 빈을 참조하는 기능
	* 메소드 호출과 객체의 프로퍼티 액세스
	* 값에서의 수학적인 동작, 관계와 관련된 동작, 논리연산 동작
	* 정규표현식 매칭
	* 컬렉션 처리

##### SpEL예제
* SpEL 표현식은 프레임이 #{...} 형식으로 구성되며, 프로퍼티 플레이스홀더는 ${...} 형식으로 프렝미이 만들어진다.
* ``#{}``을 빼면 SpEL 표현식의 몸체며 숫자 상수다.

```
#{T(System).currentTimeMillis()}
```

* 이 표현식은 시간을 밀리초 단위로 나타낸다.
* T() 연산자는 System의 타입에 대해 평가하고 static currentTimeMillis() 메소드를 수행한다.
* SpEL 표현식은 해당 빈에 다른 빈 또는 프로퍼티를 참조한다.
* 다음 표현식은 ID가 sgtPeppers인 빈의 artist프로퍼티 값을 참조한다.

```
#{sgtPeppers.artist}
```

* 프로퍼티가 주입될 때 컴포넌트 스캐닝을 통해 생성되는 빈의 생성자 인수가 주입될 때 프로퍼티 플레이스 홀더를 사용해 이전에 본 바와 같이 @Value 애너테이션을 사용한다.

```JAVA
public BlankDisc(
	@Value("#{systemProperties['disc.title']}") String title,
    @Value("#{systemProperties['disc.artist']}") String artist) {
    this.title = title;
    this.artist = artist;
}
```

* XML 설정에서 SpEL 표현식을 사용해 property 또는 constructor-arg의 value 애트리뷰트나, p, c 네임스페이스의 값으로 주어진다.

```XML
<bean id="sgtPeppers"
	class="soundsystem.BlankDisc"
    c:_title="#{systemProperties['disc.title']}"
    c:_artist="#{systemProperties['disc.artist']}" />
```

##### 리터럴 값 표시하기
* SpEL을 사용해 리터럴 정수, 부동소수점, String, 불리언 값도 표기 가능하다.

##### 빈, 프로퍼티, 메소드 참조
* SpEL 표현식의 또 다른 역할은 ID를 이용한 다른 빈의 참조다.
	* ``#{sgtPeppers}``

* 표현식에서 sgtPeppers 빈의 artist 프로퍼티를 참조하는 경우
	* ``#{sgtPeppers.artist}``

* ID가 artistSelector인 다른 빈의 selectArtist() 메소드를 호출할 경우
	* ``#{artistSelector.selectArtist()}``

* selectArtist()가 널을 반환하는 것을 방지하기 위해 타입세이프 연산자를 사용하는 경우
	* ``#{artistSElector.selectArtist()?.toUpperCase()}``

* toUpperCase() 메소드의 경우 점(.) 대신 ? 연산자를 사용한다.
* 오른쪽으로 접근하기 전 왼쪽에 있는 아이템이 널이 아닌지 확인한다.
* 만약 널이라면 selectArtist()가 toUpperCase()메소드를 호출하지 않는다.

##### 표현식에서 타입 사용하기
* 하나의 예로 자바의 Math클래스를 SpEL에서 표현하려면 다음과 같ㅌ이 T() 연산자를 사용해야 한다.
	* ``T(java.lang.Math)``

##### SpEL 연산자
* SpEL은 다양한 연산을 제공한다.

![그림](C:\Users\Tim\Desktop\Timulys\study\subject\SpringInAction\img\표3.1.png)

##### 정규표현식 사용하기
* SpEL은 matches 연산자로 표현식의 패턴 매칭을 제공한다.
	* ``#{admin.email matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com'}

##### 컬렉션 사용하기
* SpEL은 컬렉션과 배열을 이용하 작업할 수 있따.
* 리스트 내 하나의 요소를 사용한 참조
	* ``#{jukebox.songs[4].title}``
