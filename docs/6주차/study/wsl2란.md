
# WSL2(Windows Subsystem for Linux 2) 설명

## 1. WSL2란?
WSL2는 **Windows Subsystem for Linux 2**의 줄임말로,
Windows 환경에서 리눅스를 가상머신처럼 실행할 수 있게 해주는 기능입니다.

### 핵심 포인트
- 💡 **윈도우 안에서 리눅스를 실행**할 수 있도록 해주는 기술
- Docker와 같은 리눅스 기반 프로그램을 Windows에서 실행 가능
- WSL1 대비 속도와 호환성이 크게 향상됨

---

## 2. Docker에서 WSL2가 필요한 이유
- Docker는 원래 **리눅스 커널** 위에서 동작
- Windows에는 리눅스 커널이 없으므로 Docker가 동작하기 위해 WSL2가 필요
- WSL2는 Windows에 **리눅스 커널**을 추가하여 Docker가 마치 리눅스에서 실행되는 것처럼 만들어줌
- 예전에는 Hyper-V 가상화를 사용했지만, WSL2가 훨씬 가볍고 빠름

---

## 3. WSL2 설치 방법 (Windows 10/11)

1. **PowerShell 관리자 모드 실행**
2. 다음 명령어 입력:
   ```powershell
   wsl --install
   ```
3. 설치 완료 후 **재부팅**
4. 리눅스 배포판(Ubuntu 등) 선택 및 초기 설정

---

## 4. WSL 버전 확인
```powershell
wsl --version
```
또는
```powershell
wsl -l -v
```
- `VERSION`이 2면 WSL2 사용 중
- 1이면 다음 명령어로 변경:
  ```powershell
  wsl --set-version Ubuntu 2
  ```

---

## 5. 요약
- WSL2 = Windows 안에 리눅스를 실행하는 가벼운 가상머신
- Docker가 Windows에서 정상 작동하려면 필수
- 설치와 사용이 간단하고 속도도 빠름
