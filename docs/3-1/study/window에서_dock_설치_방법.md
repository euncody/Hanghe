
# Windows에서 Docker + WSL2 세팅 가이드

## 1단계: WSL2 설치
1. **PowerShell 관리자 모드 실행**
   - 시작 버튼 → "PowerShell" 검색 → "관리자 권한으로 실행"

2. **WSL 설치 및 Ubuntu 배포판 설치**
   ```powershell
   wsl --install
   ```
   - 자동으로 WSL2와 최신 Ubuntu를 설치합니다.

3. **설치 후 재부팅**

4. **Ubuntu 첫 실행 시 사용자 이름과 비밀번호 설정**

---

## 2단계: WSL 버전 확인
PowerShell에서:
```powershell
wsl -l -v
```
- 설치된 Ubuntu 옆에 **VERSION 2**가 표시되면 정상
- 만약 1로 표시되면:
  ```powershell
  wsl --set-version Ubuntu 2
  ```

---

## 3단계: Docker Desktop 설치
1. [Docker 공식 사이트](https://www.docker.com/products/docker-desktop) 접속
2. **Download for Windows – AMD64** 선택 (일반적인 PC 기준)
3. 설치 시 **"Use the WSL 2 based engine"** 체크

---

## 4단계: WSL에서 Docker 사용 설정
1. Docker Desktop 실행 → **Settings → Resources → WSL Integration** 이동
2. 설치된 Ubuntu 배포판 **ON**

---

## 5단계: Docker 정상 동작 테스트
PowerShell 또는 Ubuntu에서:
```bash
docker --version
docker run hello-world
```
- `Hello from Docker!` 메시지가 나오면 성공 🎉

---

## 추가 팁
- WSL2와 Docker를 설치하면, 윈도우와 리눅스 둘 다에서 `docker` 명령어를 사용할 수 있음
- 개발할 때는 Ubuntu에서 명령어 치는 것이 속도가 더 빠름
- Docker Desktop에서 **Compose v2** 활성화 시 `docker compose up` 명령어 바로 사용 가능
