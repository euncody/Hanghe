# AOP 이해하기 🎯

## AOP가 뭔가요?

**AOP(Aspect-Oriented Programming)**는 쉽게 말해서 **"공통으로 사용하는 기능들을 따로 빼서 관리하자!"**는 아이디어입니다.

예를 들어보겠습니다:

```java
// AOP 사용 전 - 모든 메소드에 로깅 코드가 중복됨
public class UserService {
    public void saveUser(User user) {
        System.out.println("saveUser 메소드 시작"); // 로깅
        // 실제 사용자 저장 로직
        System.out.println("saveUser 메소드 끝");   // 로깅
    }
    
    public void deleteUser(Long id) {
        System.out.println("deleteUser 메소드 시작"); // 로깅
        // 실제 사용자 삭제 로직  
        System.out.println("deleteUser 메소드 끝");   // 로깅
    }
}
```

이렇게 하면 로깅 코드가 계속 반복됩니다. 😥

```java
// AOP 사용 후 - 로깅은 따로 관리!
@Service
public class UserService {
    
    @LogExecutionTime  // 이것만 붙이면 로깅 완료!
    public void saveUser(User user) {
        // 실제 사용자 저장 로직만 작성
    }
    
    @LogExecutionTime  // 이것만 붙이면 로깅 완료!
    public void deleteUser(Long id) {
        // 실제 사용자 삭제 로직만 작성
    }
}
```

## 왜 AOP를 사용하나요? 🤔

### 문제상황
우리가 웹 서비스를 만든다고 생각해보세요. 모든 메소드에 이런 기능들이 필요합니다:

- 📝 **로깅**: "이 메소드가 언제 실행됐는지 기록하자"
- 🔒 **보안**: "이 사용자가 권한이 있는지 확인하자"  
- ⏱️ **성능측정**: "이 메소드가 얼마나 빨리 실행되는지 측정하자"
- 💾 **트랜잭션**: "데이터베이스 작업이 안전하게 되도록 하자"

이런 기능들을 모든 메소드에 하나하나 작성한다면... 😱

### AOP의 해결책
**"공통 기능은 따로 빼서, 필요한 곳에 자동으로 적용하자!"**

마치 스티커 붙이듯이 간단하게!

## 핵심 개념을 쉽게 이해하기 📚

### 1. Aspect (관점)
**공통 기능을 모아놓은 클래스**

```java
@Aspect  // "나는 공통 기능이야!"
public class LoggingAspect {
    // 로깅 관련 기능들을 여기에 모아놓음
}
```

### 2. Pointcut (어디에?)
**"어떤 메소드들에 적용할지 정하는 것"**

```java
@Pointcut("execution(* com.example.service.*.*(..))")
// "service 패키지의 모든 클래스, 모든 메소드에 적용해!"
```

쉽게 말하면 **"타겟 설정"** 같은 거예요!

### 3. Advice (언제?)
**"언제 실행할지 정하는 것"**

- **@Before**: 메소드 실행 **전에**
- **@After**: 메소드 실행 **후에**  
- **@Around**: 메소드 실행 **전후 모두**

```java
@Before("serviceLayer()")  // 메소드 실행 전에
public void logBefore() {
    System.out.println("메소드가 시작됩니다!");
}
```

## 실제 예시로 이해하기 💡

### 예시 1: 실행 시간 측정하기

```java
@Aspect
@Component
public class TimeCheckAspect {
    
    // Service의 모든 메소드에 적용
    @Around("execution(* com.example.service.*.*(..))")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        
        long start = System.currentTimeMillis();  // 시작 시간
        
        Object result = pjp.proceed();  // 실제 메소드 실행
        
        long end = System.currentTimeMillis();    // 끝 시간
        
        System.out.println(pjp.getSignature().getName() + 
                          " 실행시간: " + (end - start) + "ms");
        
        return result;
    }
}
```

이렇게 하면 Service의 **모든 메소드**에 자동으로 실행 시간이 출력됩니다! 🎉

### 예시 2: 간단한 로깅

```java
@Aspect  
@Component
public class SimpleLoggingAspect {
    
    @Before("execution(* com.example.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("🚀 " + joinPoint.getSignature().getName() + " 메소드 시작!");
    }
    
    @After("execution(* com.example.controller.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("✅ " + joinPoint.getSignature().getName() + " 메소드 완료!");
    }
}
```

## Spring에서 AOP 사용하기 🌱

### 1. 의존성 추가 (build.gradle)
```gradle
implementation 'org.springframework.boot:spring-boot-starter-aop'
```

### 2. AOP 활성화
```java
@SpringBootApplication
@EnableAspectJAutoProxy  // AOP 사용하겠다고 선언
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### 3. Aspect 클래스 만들기
```java
@Aspect
@Component  // 스프링이 관리하도록
public class MyAspect {
    // 여기에 공통 기능 작성
}
```

## AOP의 장점 👍

1. **코드 중복 제거**: 같은 코드를 여러 번 안 써도 됨
2. **관심사 분리**: 비즈니스 로직에만 집중 가능
3. **유지보수 쉬움**: 공통 기능 수정할 때 한 곳만 바꾸면 됨
4. **재사용성**: 다른 프로젝트에서도 사용 가능

## AOP의 단점 😅

1. **처음에는 복잡해 보임**: 코드 흐름 파악이 어려울 수 있음
2. **디버깅 어려움**: 실제 실행되는 코드와 보이는 코드가 다름
3. **성능**: 프록시 생성으로 약간의 성능 저하

## 언제 사용하면 좋을까? 🤷‍♂️

### 사용하면 좋은 경우
- 로깅이 필요한 모든 메소드
- 권한 체크가 필요한 모든 메소드  
- 트랜잭션이 필요한 모든 메소드
- 캐싱이 필요한 모든 메소드

### 사용하지 않는 게 좋은 경우
- 한두 곳에서만 사용하는 기능
- 성능이 매우 중요한 부분
- 팀원들이 AOP를 잘 모르는 경우

## 쉬운 시작 방법 🚀

### 1단계: 로깅부터 시작
```java
@Aspect
@Component  
public class BasicLoggingAspect {
    
    @Before("execution(* com.yourpackage.controller.*.*(..))")
    public void logControllerMethods(JoinPoint joinPoint) {
        System.out.println("▶️ " + joinPoint.getSignature().getName());
    }
}
```

### 2단계: 실행 시간 측정 추가
```java
@Around("execution(* com.yourpackage.service.*.*(..))")
public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object proceed = joinPoint.proceed();
    long executionTime = System.currentTimeMillis() - start;
    System.out.println(joinPoint.getSignature() + " 실행시간: " + executionTime + "ms");
    return proceed;
}
```

### 3단계: 애노테이션으로 더 쉽게
```java
// 커스텀 애노테이션 만들기
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTime {
}

// 애노테이션이 붙은 메소드에만 적용
@Around("@annotation(LogTime)")
public Object logTimeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
    // 실행 시간 측정 로직
}
```

```java
// 사용할 때
@Service
public class UserService {
    
    @LogTime  // 이것만 붙이면 끝!
    public void saveUser(User user) {
        // 비즈니스 로직만 작성
    }
}
```

## 마무리 🎯

AOP는 **"공통 기능을 쉽게 관리하자"**는 아이디어입니다.

처음에는 복잡해 보이지만, 한 번 익숙해지면 코드가 훨씬 깔끔해지고 관리하기 쉬워집니다!