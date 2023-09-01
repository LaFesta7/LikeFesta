# LaFesta

![la](https://github.com/LaFesta7/LikeFesta/assets/132440453/08557e8d-1347-462c-8749-e8572f0f68ce)

7조 최종프로젝트 '라페스타'

## 1. 프로젝트 개요

지도 API를 활용하여 페스티벌,
축제, 행사, 공연 등의 위치를 한 눈에 볼 수 있으며,
페스티벌의 정보와 리뷰를 확인할 수 있는 웹 서비스 입니다.

## 2. 기술 스택 및 개발 환경

### 2.1. 기술 스택

- Front-end : Ajax, Thymeleaf, Html, CSS, JavaScript, 카카오 지도 Api
- Back-end : Spring Boot, Spring Security, Spring Data JPA
- Database : MySQL
- IDE : IntelliJ IDEA Ultimate
- SCM : Git, Github
- Communication : Slack, Kakaotalk, Gather
- Design : Figma, Canvan
- Deploy : AWS EC2, AWS RDS
- ETC : Notion

### 2.2. 개발 환경

- JDK 17
- Spring Boot 3.1.2
- Spring dependency-management 1.1.2
- JUnit 5.9.3
- Gradle 8.2.1
- JJWT 0.11.5
- Swagger UI 4.15.5
- Spring Security 6.1.2

### 2.3. 기술적 의사 결정

#### 2.3.1. 알림 기능

    메세지 큐를 활용한 비동기 통신 
    vs 웹 소켓을 사용한 실시간 알림 
    vs 스케줄링을 통한 주기적 알림 
    vs 이메일 또는 푸시 알림 서비스와 연동 
    vs 데이터베이스를 활용한 저장 및 조회
    
    ->이메일과 DB 저장 - 조회를 사용하여 스케줄링 및 이벤트로 진행 

#### 2.3.2. Refresh Token -> Redis 도입

## 3. API 설계

![a-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/4a4bb893-aa86-47d6-95e2-6a4f5fdc355f)
![b-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/20ae5c2b-695a-49c4-a9c2-1890303630bd)
![c](https://github.com/LaFesta7/LikeFesta/assets/132440453/d9bba998-0911-49a0-8f6c-1132bd9eeb86)
![d-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/9f9e516f-c55b-4c88-8f2c-3a45b7ef811b)
![e-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/1022aeae-7b85-46c2-a0a1-a2126617546e)
![f-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/eaa7a39e-2b6c-4ab2-8ed9-a81b64dfd575)
![g-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/fffea5ce-2d0c-453a-a5e9-6a21675b1bfc)
![h-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/9fd797a5-645c-4116-b17a-d82ca36bac3e)

## 4. ERD 설계

![lafesta_erd-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/dbc7924f-fa5d-441c-bfa6-24c7a4d0e17b)

## 5. 와이어프레임 설계

![Group_1](https://github.com/LaFesta7/LikeFesta/assets/131860214/5522cba4-d879-4fdf-b41b-084fe7736bc1)

![Group_2](https://github.com/LaFesta7/LikeFesta/assets/131860214/b62477f5-4580-4fd0-93ad-899f8433483a)
![Group_3](https://github.com/LaFesta7/LikeFesta/assets/131860214/995e1e0e-e7b5-4d65-88fd-85f6c6db1d67)
![Group_4](https://github.com/LaFesta7/LikeFesta/assets/131860214/bc96e3f7-1aef-4eff-8a6d-4156d845727a)
![Group_5](https://github.com/LaFesta7/LikeFesta/assets/131860214/8945476f-e51f-4d20-994f-50fc8196ff30)
![Group_6](https://github.com/LaFesta7/LikeFesta/assets/131860214/ad4efa1c-d73f-4c1c-b030-b7d25b2c1167)
![Group_7](https://github.com/LaFesta7/LikeFesta/assets/131860214/267c3c3e-5f09-4ce6-86f8-11bd96942acd)
![Group_8](https://github.com/LaFesta7/LikeFesta/assets/131860214/43790f05-3d53-49e1-b09a-070407149777)

## 6. 트러블 슈팅

### 6.1. 트러블 슈팅

#### 6.1.1. 문제 상황

    Hibernate의 Lazy Loading으로 인한 오류

    - 문제 상황 : org.hibernate.LazyInitializationException 예외가 발생. 
    이는 Hibernate의 Lazy Loading 기능을 사용할 때 발생하는 문제. 
    이 오류는 엔티티의 연관 관계를 지연로딩(Lazy Loading)으로 설정했을 때, 
    실제 데이터를 조회하지 않은 상태에서 연관된 컬렉션을 접근하려고 할 때 발생.
    festival에서 user를 갖고와 user에서 follower들을 가져오려고 하니 발생한 문제였다.

    - 문제 해결 : 레포지토리에서 직접 가져오니 문제 해결

#### 6.1.2. 문제 상황

    CI 환경변수 설정 문제

    - 문제 상황 : 기존 application-test.properties로 환경 변수를 추가할 경우 
    application.properties에서 주입받을 application-s3.properties가 생성되지 않아 CI가 돌아가지 않음 
    -> application-s3.properties를 생성하도록 코드 수정

    - 문제 해결 : dev-ci.yml 파일을 수정하여 해결

    - 추가 수정 1 : MYSQL 서비스가 테스트 환경에서만 외부에서 임시로 사용하는 서비스긴 하지만 
    정보가 숨겨져 있는것이 더 안전하다고 판단되어 해당 부분 환경변수로 수정

    - 추가 수정 2 : 이전에 추가한 build with Gradle 단이 아래 있던 어플리케이션 실행 테스트 단으로 
    대체가 가능하다는 것을 알게되어 필요하지 않은 build with Gradle은 삭제 진행함

#### 6.1.3. 문제 상황

    변수명 ‘예약어’로 인한 오류

    - 문제 상황 : read 예약어로 인해 Notification Entity 테이블이 생성되지 않는 문제
    read는 MySQL에서 예약어로 사용되는 키워드 중 하나이므로 컬럼 이름으로 사용하기에는 적합하지 않다. 
    MySQL에서 키워드를 컬럼 이름으로 사용하려면 백틱(`)으로 묶어주어야 한다.

    - 문제 해결 : 백틱으로 묶어주어 해결
        ->컬럼 이름으로 사용이 적합하지 않다하여 '변수명 짓기' 사이트 참고하여 컬럼명 재작성

## 7. 프런트 엔드 레포지토리

백엔드와 합쳐서 진행

## 8. 팀원 소개 및 역할 분담

### 8.1. 팀원 소개

리더 / 부리더 : 권진혁 / 김현우

팀원 : 조해나 / 정해인 / 정지상

### 8.2. 역할 분담

권진혁 : Init 코드 작성, 인증 및 인가, User 세팅, 지도 OPEN API, 기술버전 기록, 소셜 로그인, README 1차 작성, CI, 프론트엔드 및 프론트-백
연결, 발표 자료 준비 및 중간 발표

김현우 : 기술버전 기록, 첨부파일(S3), 테스트 코드 방법 연구, Redis 연동, Refresh Token

조해나 : Init 코드 작성, 페스티벌, 리뷰, 코멘트 CRUD, User 수, Like 기능(festival, review, comment), CI, 권한 및 페스티벌 게시
요청 CRUD, 이메일 인증 개선, 유저 서비스 개선, 이메일을 활용한 알림 기능, 이벤트 퍼블리셔를 활용한 웹 알림 기능, 뱃지 기능, 프론트-백 연결, 발표 자료 준비

정해인 : Follow 기능(festival, user), 이메일 인증, 페이징 처리, 태그 및 태그별 조회 기능, 랭킹 조회, README 2차 작성

정지상 : 유저 프로필 조회, 정보 수정, 탈퇴 기능 
