# 📌 성능/부하 테스트 계획서

## 1. 목적 (Objectives)
- 상품 조회를 목표 응답시간 내 처리 가능한지 검증
- 피크·스파이크 상황에서 성능 저하 및 병목 구간(DB 락, 캐시 미스, 외부 연동 지연 등) 식별
- 동시 사용자 수 / 초당 요청 수(RPS)에 따른 시스템 용량 추정

---

## 2. 범위 (Scope)
- **In-Scope**
    - Public API / 웹 백엔드, 검색
    - 검색/목록
- **Out-of-Scope**
    - 관리자 백오피스
    - 배치/ETL 잡
    - 실제 결제/실물 DRM (모의·스텁 사용)

---

## 3. 시스템 환경 (SUT)
- 대상 도메인: http://localhost:8089
- 구성: LB → Web/API → Cache(Redis) → DB(MySQL)  
  (+ 외부 연동: Bookus/DRM 등은 가짜 API로 대체 권장)
- 조건: 테스트 전용 계정/데이터, Grafana·CloudWatch 등 모니터링 환경 확보

---

## 4. 트래픽/워크로드 모델
- 읽기 : 쓰기 비율 → 90 : 10 (가정)
- 피크 RPS 목표: **TODO (예: 300 rps)**
- 사용자 시나리오 비중
    - 검색/목록 55%

---

## 5. 테스트 시나리오 (User Journeys)
1. `GET /api/products` → 키워드 검색

---

## 6. 지표 및 목표 (Metrics & Targets)
- 오류율: `< 0.5%`
- 응답시간: `p95 ≤ 800ms`, `p99 ≤ 1200ms`
- 자원 사용률: CPU `< 70%`, 메모리 `< 80%`
- DB 대기 이벤트, 락 발생률 모니터링
- 목표 RPS 도달 여부

---

## 7. 테스트 단계 (Phases)
| 단계      | 목적               | 조건                 |
|-----------|--------------------|----------------------|
| Smoke     | 기본 동작 확인     | 1~2VU, 1분            |
| Load      | 목표 RPS까지 점증  | 30VU → 15분 유지     |
| Spike     | 단시간 급격 증가   | 100VU → 1~2분         |
| Soak      | 장시간 부하        | 30~50VU, 30~60분      |

---

## 8. 리스크 및 완화 (Risks & Mitigation)
- 실데이터 오염 → 스테이징 환경 / 테스트 계정
- 외부 연동 쿼터 소진 → 스텁/레이트리밋 적용
- WAF/IPS 차단 → 화이트리스트 및 테스트 시간 협의

---

## 9. 산출물 (Deliverables)
- k6 실행 로그(JSON/summary)
- Grafana/CloudWatch 대시보드 스크린샷
- 성능 병목 분석 및 개선안(SQL, 캐시, 스레드 등)

---

## 10. 참고
- 부하 스크립트는 `main.js`에서 작성 및 실행
- 실행 명령 예시
  ```bash
  k6 run main.js \
    -e BASE_URL=https://staging.example.com \
    -e ACCESS_USER=test01 -e ACCESS_PASS=pass01
