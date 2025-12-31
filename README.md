# 🐾 유기동물 AI 추천 시스템

  <div align="center">

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)

**사용자의 관심사를 학습하여 최적의 유기동물을 추천하는 AI 기반 매칭 서비스**

  </div>

  ---

## 📌 프로젝트 소개

매년 약 10만 마리의 유기동물이 발생하지만 입양률은 30% 미만입니다.
입양 희망자가 자신에게 맞는 동물을 찾기 어렵고, 보호소별로 정보가 분산되어 접근성이 낮습니다.

이 프로젝트는 **AI 임베딩 기반 추천 시스템**으로 사용자의 행동 패턴을 학습하여
개인에게 최적화된 유기동물을 추천하고, 전국 보호소 정보를 한 곳에서 조회할 수 있게 합니다.

<img width="1320" height="744" alt="image" src="https://github.com/user-attachments/assets/17679e3d-3cb9-4bb7-8d03-dec5e3e59290" />
<img width="1320" height="744" alt="image" src="https://github.com/user-attachments/assets/daf4115d-417d-40cf-821d-0e18bf4d704a" />
<img width="1320" height="741" alt="image" src="https://github.com/user-attachments/assets/ac90521e-7057-4d2f-8d77-ea07e7063dc5" />
<img width="1320" height="741" alt="image" src="https://github.com/user-attachments/assets/4cd092f9-1b5f-4073-ba47-52c7fe9b511b" />
<img width="1320" height="740" alt="image" src="https://github.com/user-attachments/assets/b7491f79-13b7-4d35-a62f-4554902f2112" />
<img width="1320" height="740" alt="image" src="https://github.com/user-attachments/assets/ac5b527f-7e91-490d-9ffa-fc97b62984be" />
<img width="1318" height="741" alt="image" src="https://github.com/user-attachments/assets/3559cdd7-9b3c-4487-8941-b3a036f958e4" />
<img width="1321" height="742" alt="image" src="https://github.com/user-attachments/assets/2ea854c8-50a3-4b1a-9992-36d0fecb5ef0" />


**주요 기능**:
- 사용자 행동 학습 기반 AI 추천
- 지역/품종/나이별 유기동물 검색
- 입양 관련 RAG 챗봇
- 카카오 소셜 로그인

  ---

### 전역 예외 처리 설계

**적용한 설계**:
- `@RestControllerAdvice`로 전역 예외 처리
- 비즈니스 예외(`GeneralException`)와 시스템 예외 구분
- 사용자 친화적인 에러 메시지 통일

  ---

### PR 컨벤션 통일
  ```
  요약(Summary)
  PR 유형
  공유사항 to 리뷰어
  PR Checklist
  ```
  ---

### 커밋 컨벤션 통일

**적용한 규칙**:
  ```
  feat/#이슈번호: 기능 추가
  fix/#이슈번호: 버그 수정
  refactor/#이슈번호: 리팩터링
  ```

**효과**:
- 커밋 히스토리만 봐도 프로젝트 진행 상황 파악 가능
- 이슈 번호로 맥락 추적 용이
- 나중에 특정 기능 찾을 때 검색 편리

  ---

### Clean Architecture 구조 설계

**목표**:
계층 간 의존성을 명확히 하여 유지보수성 향상

**적용한 구조**:
  ```
  api (Controller, DTO)         <- 외부 인터페이스
    
  application (Service)         <- 비즈니스 로직
    
  domain (Entity, Repository)   <- 핵심 도메인
  ```

**결과**:
- 각 계층의 책임이 명확
- 테스트 작성 용이
- 새로운 기능 추가 시 영향 범위 최소화

  ---

## 🛠️ 기술 스택

### Backend
- Java 21, Spring Boot 3.5.7
- JPA + QueryDSL
- Spring Security + JWT

### Database & Infrastructure
- MySQL 8.0
- AWS EC2, S3
- Docker, GitHub Actions (CI/CD)

### External APIs
- 공공 데이터 포털 (유기동물 API)
- Upstage Solar (RAG)
- Kakao OAuth 2.0

  ---

## API 명세

### 주요 엔드포인트

  ```
  GET  /api/animals               # 유기동물 검색
  GET  /api/animals/{id}          # 상세 조회
  POST /api/animals/interests     # 사용자 관심 기록
  GET  /api/recommendations       # AI 추천
  POST /api/rag/chat              # 챗봇 대화
  GET  /api/auth/kakao            # 로그인
  ```
