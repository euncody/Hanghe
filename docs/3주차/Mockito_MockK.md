# Mockito vs MockK

> **Mockito**와 **MockK**는 테스트에서 의존 객체를 "mocking" 하기 위한 도구입니다. Kotlin 기반 프로젝트에서는 `MockK`가 선호되는 경우가 많고, Java 기반에서는 `Mockito`가 일반적입니다.

---

## ✅ 요약 비교표

| 항목             | **Mockito**                            | **MockK**                              |
| -------------- | -------------------------------------- | -------------------------------------- |
| 주 사용 언어        | Java 기반 프로젝트에 최적화                      | Kotlin에 최적화                            |
| Kotlin 지원      | 제한적 (final class, extension 지원 불편)     | Kotlin 특성 (final class, 확장 함수 등) 완벽 지원 |
| 기본 Mock 방식     | `@Mock`, `when { ... } thenReturn ...` | `@MockK`, `every { ... } returns ...`  |
| 정적 메서드 mock    | 별도 도구 필요 (`PowerMock`)                 | 자체 지원 (`mockkStatic()`)                |
| Null safety 지원 | 미흡 (nullable type 지원 불편)               | Kotlin null-safe API 지원                |
| 사용 편의성         | Java 스타일, 약간 장황함                       | Kotlin 친화적 DSL 스타일, 간결함                |
| Spring과 통합     | Spring Boot 공식 지원 (`@MockBean`)        | `@MockKBean`은 별도 설정 필요                 |

---

## ✅ Mockito 예제 (Java/Kotlin 같이 사용 가능)

```kotlin
val userService = mock(UserService::class.java)
whenever(userService.getUserInfo(1L)).thenReturn(UserResponse(1L, "홍길동", 1000))
```

---

## ✅ MockK 예제 (Kotlin에 최적화)

```kotlin
val userService = mockk<UserService>()
every { userService.getUserInfo(1L) } returns UserResponse(1L, "홍길동", 1000)
```

---

## ✅ 어떻게 선택할까?

| 상황                           | 최고의 라이브러리 | 이유                           |
| ---------------------------- | --------- | ---------------------------- |
| Java 기반 프로젝트                 | ✅ Mockito | Spring + Java 개발자에게 관련 자료 많음 |
| Kotlin 프로젝트                  | ✅ MockK   | DSL 문맥, final/extension 지원   |
| Spring + @MockBean 지원이 필요할 때 | ✅ Mockito | 공식 호환성 포함                    |
| Kotlin + 반환속도 해야할 때          | ✅ MockK   | Kotlin의 null-safe 개칭에 반환     |

---

## 해당 MockK의 강점

1. Kotlin `data class`, `final class` mocking 가능
2. `mockkStatic`, `mockkObject` 개체/경우에서 특수 함수 mocking 가능
3. `slot`, `capture` 을 이용한 인자 확인

---

## ✅ 결말

> Kotlin을 주로 사용하는 프로젝트에서는 **MockK**를 사용하는 것이 자주 권장됩니다.
> Spring Boot + Java 주의일 경우는 **Mockito**가 더 호심됩니다.

