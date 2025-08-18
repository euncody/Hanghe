
# docker-compose.yml 이란?

## 1. 정의
`docker-compose.yml`은 **여러 개의 Docker 컨테이너를 한 번에 정의하고 실행할 수 있게 하는 설정 파일**입니다.
쉽게 말해 **"내 서비스 환경 설계도"** 역할을 합니다.

---

## 2. 왜 사용하나?
- `docker run` 명령어로도 컨테이너를 실행할 수 있지만, 서비스가 많아지면 명령어가 길어지고 관리가 어려워짐
- `docker-compose.yml`에 환경을 미리 정의해두면
  - **`docker compose up` 한 번**으로 여러 컨테이너를 동시에 실행 가능
  - 네트워크, 볼륨, 환경변수 등을 자동으로 설정

---

## 3. 기본 구조 예시
```yaml
version: "3.9"  # Compose 파일 버전

services:
  db:  # 서비스 이름 (PostgreSQL)
    image: postgres:15
    container_name: my_postgres
    restart: always
    environment:
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypass
      POSTGRES_DB: mydb
    ports:
      - "5432:5432"
    volumes:
      - db_data:/var/lib/postgresql/data

  app:  # 서비스 이름 (Spring Boot)
    build: .
    container_name: my_spring_app
    ports:
      - "8080:8080"
    depends_on:
      - db

volumes:
  db_data:
```

---

## 4. 실행 방법
```bash
# 컨테이너 생성 & 실행
docker compose up -d

# 로그 보기
docker compose logs -f

# 중지
docker compose down
```

---

## 5. 장점
- 여러 서비스(DB, API 서버, 캐시 서버 등)를 한 번에 관리 가능
- 네트워크 자동 구성
- 환경변수, 볼륨 설정이 간단
- 개발 환경을 손쉽게 복제 가능

---

## 6. 한 줄 정의
> `docker-compose.yml`은 여러 Docker 컨테이너를 **한 번에 정의하고 실행**할 수 있는 환경 설정 파일입니다.
