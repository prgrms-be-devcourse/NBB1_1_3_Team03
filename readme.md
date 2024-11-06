# 프로젝트 개요
> ### [2차프로젝트](https://github.com/prgrms-be-devcourse/NBE1_2_Team03)를 Kotlin으로 마이그레이션한 프로젝트입니다.
<br>
<br>

# <img src='https://github.com/user-attachments/assets/934c6b83-ca20-4eec-b218-47b4af5322ae' width='270px' align=left>
# SCANNER
일반 쓰레기통 및 담배꽁초 쓰레기통 위치 제공 서비스

<br>
<br>
<br>
<br>
<br>

## 🗑️ SCANNER를 소개합니다.

SCANNER는 사용자에게 일반 쓰레기통 및 담배꽁초 전용 쓰레기통 위치를 제공하여 조금이나마 쓰레기를 올바르게 처리할 수 있도록 유도하였습니다.

수많은 일반 쓰레기통 위치를 확인하고 쓰레기통이 부족하다는 인식을 완화할 수 있으며, 
담배꽁초로 발생하는 화재 및 환경 오염 문제를 헤결 하고자 하였습니다.


---

## 1. 세부 기능

### **`Step 01`. 로그인을 진행합니다.**
- **로그인**
    - CoolSMS API를 활용한 문자 인증 기능을 도입하여 보다 안전한 회원가입을 할 수 있습니다.

### **`Step 01`. 쓰레기통 위치를 지도로 확인할 수 있습니다.**
- **위치 확인**
    - 사용자의 위치 200m 전방의 모든 쓰레기통 위치를 지도를 통해 확인할 수 있습니다.
    - Elasticsearch를 활용한 검색 기능을 통해 보다 빠르게 쓰레기통 위치를 찾을 수 있습니다.
    - 쓰레기통 세부 정보(쓰레기통 상태, 사진 등)를 확인할 수 있습니다.

### **`Step 02`. 신고 게시글을 작성할 수 있습니다.**
- **신고 게시글 작성**
    - 새로운 쓰레기통을 발견하면 쓰레기통 위치 추가 신고 게시글을 작성할 수 있습니다.
    - 쓰레기통 상태가 수정되면 쓰레기통 수정 신고 게시글을 작성할 수 있습니다.
    - 쓰레기통 제거를 원하시면 쓰레기통 삭제 신고 게시글을 작성할 수 있습니다.
    - 신고 게시글이 서비스에 반영되면 포인트를 얻을 수 있습니다.

### **`Step 03`. 포인트를 통해 다양한 상품을 구매할 수 있습니다.**
- **상품 구매**
    - 사용자는 얻은 포인트를 통해 쓰레기통과 관련된 상품을 구매할 수 있습니다. 
    - 상품은 바코드로 지급됩니다.

---

## 2. ERD

<br>
<div align="center"><img align="center" width="900" alt="ERD" src="https://github.com/user-attachments/assets/b88e5979-3a00-4920-b54a-39c77fc4ae38"></div>

---

## 3. Wireframe

<br>
<div align="center"><img align="center" width="900" alt="Wireframe" src="https://github.com/user-attachments/assets/7053b947-baee-4a08-a62b-f2232a57c875"></div>

[와이어프레임 링크](https://www.figma.com/design/mDuyOOa2s60UL6KPcnYzjZ/%ED%94%84%EB%A1%9C%EB%9E%98%EB%A8%B8%EC%8A%A42%EC%B0%A8%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?node-id=0-1&node-type=canvas&t=zCBTxtdfTlen1OZi-0)

---

## 4. 기술 스택
### Language
* Kotlin 2.0.21
* Python 3

### Framework
* Spring Boot 3
* Data JPA
* Spring Security
* React

### Database
* MySQL
* Redis
* Elasticsearch Document

### DevOps
* Github Actions
* AWS
* Docker

### Monitoring Tool
* Promtail + Loki + Grafana
* Prometheus + Grafana

### Collaboration Tool
* Github
* Notion
* Slack

---

## 5. 깃 컨벤션

> ## 💡 Commit 규칙
> ### ✅ Title(제목) = `<type>`: <커밋에 대한 간략한 설명>
> ### Type
> * `feat` : 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
> * `fix` : 기능에 대한 버그 수정
> * `build` : 빌드 관련 수정
> * `chore` : 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore
> * `cicd` : CI 관련 설정 수정
> * `docs` : 문서(주석) 수정
> * `style` : 코드 스타일, 포맷팅에 대한 수정
> * `refactor` : 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경
> * `test` : 테스트 코드 추가/수정
> * `release` : 버전 릴리즈
> ### ✅ Body(본문)
> 본문에는 다음을 유의하여 작성합니다.
> * 본문 내용은 양에 구애받지 않고 최대한 상세히 작성한다.
> * 어떻게보다는 무엇을, 왜 변경했는지를 설명한다.
> * 항목이 여러 개일 시 '_'를 통해 구분해서 작성한다.
> ### ✅ 예시
> 
> ```text
> feat: Add user authentication via Google OAuth
> - 사용자 인증을 위해 Google OAuth를 추가함
> - 기존 로컬 로그인 방식과 함께 사용 가능하도록 병행 지원
> - Google OAuth를 선택한 사용자의 프로필 정보(이름, 이메일)를 DB에 저장
> - 보안을 강화하기 위해 OAuth 로그인 시 자동으로 HTTPS로 리다이렉트 되도록 설정함
> ```

> ## 💡Issue 작성 규칙
> ### ✅ 제목 = [이슈 타입]: 제목
> ### 이슈 타입
> * **[BUG]**: 버그 리포트
> * **[FEATURE]**: 새로운 기능 요청
> * **[DOCS]**: 문서 관련 작업
> * **[TEST]**: 테스트 관련 작업
> * **[QUESTION]**: 질문 또는 논의
> 
> ### 예시
> [FEATURE] Add user profile page
> 
> ### ✅ 본문
> 본문은 다음의 내용을 포함해야 합니다.
> * 만들고자 하는 기능
> * 기능을 구현하기 위해 해야 할 일
> 
> ### 템플릿
> 
> ```text
> ### 만들고자 하는 기능
> ex) Todo 생성 기능
>
> ### 해당 기능을 구현하기 위해 할 일
> - [ ] Job1
> - [ ] Job2
> - [ ] Job3
> ```
> 
> ### ✅ 라벨
> 이슈의 가독성을 위해서 라벨을 사용합니다.
> 두 가지 종류의 라벨이 존재합니다.
> 1. 이슈 타입
> 2. 담당자

> ## 💡 PR 규칙
> * resolved : #issue 번호 추가
> * PR 시 2명에게 코드 리뷰 받고 병합
> * develop에 PR 병합 시 팀원들에게 공유하기, 각자 진행 중인 브랜치에 리베이스 하기
> 
> ### 예시
> 
> ```text
> resolved : #1
> ## 📌 과제 설명 <!-- 어떤 걸 만들었는지 대략적으로 설명해주세요 -->
> 
> 👩‍💻 요구 사항과 구현 내용 <!-- 기능을 Commit 별로 잘개 쪼개고, Commit 별로 설명해주세요 -->
> 
> ## ✅ 피드백 반영사항  <!-- 지난 코드리뷰에서 고친 사항을 적어주세요. 재PR 시에만 사용해 주세요! (재PR 아닌 경우 삭제) -->
> 
> ## ✅ PR 포인트 & 궁금한 점 <!-- 리뷰어 분들이 집중적으로 보셨으면 하는 내용을 적어주세요 -->
> ```

> ## 💡 Branch 전략
> ### Github Flow 방식
> * Main Branch: 배포 가능한 상태의 안정적인 코드가 유지되는 브랜치로, 항상 최신의 안정된 버전을 반영합니다.
> * Develop Branch: 새로운 기능 개발과 버그 수정을 위한 통합 브랜치로, 개발 중인 코드가 모이는 곳입니다.
> * Feature Branch: 특정 기능이나 작업을 위해 독립적으로 개발하는 브랜치로, 완료 후 develop 브랜치에 병합됩니다.
> 
> ### Branch명 규칙
> 예시) feature/123-add-user-login
> * feature/: 새로운 기능 개발
> * 123: 이슈 트래커에서 발급된 이슈 번호
> * add-user-login: 사용자 로그인 기능 추가

---

## 6. 서버 아키텍쳐

<br>
<div align="center"><img align="center" width="900" alt="Wireframe" src="https://github.com/user-attachments/assets/64a4286b-d7aa-4c33-88cc-ab0e3d0b2dec"></div>

---
<br>

## 7. 코틀린 마이그레이션 중 이슈
### 🚨 널 안전성 문제
- 코틀린은 변수에 널 값을 허용x  ➡ 프로퍼티 초기화 /생성자 주입  

### 🚨생성자 주입 방식
- 코틀린은 주로 @Autowired 대신 생성자 주입을 사용  val을 사용해 주입 가능 

### 🚨Configuration과 Bean 선언
- @Configuration @Bean 선언 시  fun 사용
 ➡ 코틀린  타입 추론이 제대로 작동하지 않는 경우 반환 타입을 명시적 선언

### 🚨Builder 대체 수단 필요
- apply , copy 메서드, 정적 메서드 등을 활용해 구현

### 🚨JPA  코틀린 data class 호환
- data class 는 JPA 엔티티로 적합 x   ➡ 일반 클래스 사용
- @Entity에는 기본생성자,  open 키워드 사용 -> gradle open 옵션 자동 기능 설정

### 🚨Lombok 코틀린 사이 호환 문제
- 롬복 사용 제한  ➡ 자체 지원되는 data class , val, var 키워드 통해 롬복 사용 최소화(제거)

### 🚨 Checked Exception 처리
- 코틀린은 Checked Exception을 강제 x  ➡ 예외 처리가 누락되지 않도록 주의  

--- 

<br>

## 8. 개발자들
<table>

  <td align=center>
  <a href="https://github.com/l2yujw">
  <img src="https://avatars.githubusercontent.com/u/49338509?v=4" width="100px"  />
  <br/>
  BackEnd 🖥
  <br/>
  류정원
  </a>
  </td>

  <td align=center>
  <a href="https://github.com/7zrv">
  <img src="https://avatars.githubusercontent.com/u/90759319?v=4" width="100px"  />
  <br/>
  BackEnd 🖥
  <br/>
  윤서진
  </a>
  </td>

  <td align=center>
  <a href="https://github.com/Leehunil">
  <img src="https://avatars.githubusercontent.com/u/104710245?v=4" width="100px"  />
  <br/>
  BeckEnd 🖥
  <br/>
  이훈일
  </a>
  </td>

  <td align=center>
  <a href="https://github.com/jieun5119">
  <img src="https://avatars.githubusercontent.com/u/119848830?v=4" width="100px"  />
  <br/>
  BackEnd 🖥
  <br/>
  김지은
  </a>
  </td>

  <td align=center>
  <a href="https://github.com/jhk01007">
  <img src="https://avatars.githubusercontent.com/u/75934142?v=4" width="100px"  />
  <br/>
  BackEnd 🖥
  <br/>
  김재희
  </a>
  </td>

</table>
