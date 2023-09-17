# 🎉 LaFesta

7조 최종프로젝트 '라페스타'

<br>

## 1. 프로젝트 개요

#### " 페스티벌 시작과 끝, 정보와 커뮤니티를 한번에! "
'LaFesta'는 지도 API를 활용하여 마커로 페스티벌 위치를 표시하고,
페스티벌 정보와 리뷰를 공유하는
엔터테이먼트 웹서비스 입니다.
<br>

### [→ 라페스타 서비스 보러가기](http://lafesta.site/)
### [→ 라페스타 시연영상 보러가기](https://www.youtube.com/watch?v=Yo1aidZtxkg)
### [→ 라페스타 발표자료 보러가기](https://www.canva.com/design/DAFuYWiRXCQ/237flMN085Tv00Urxvs_MA/view?utm_content=DAFuYWiRXCQ&utm_campaign=designshare&utm_medium=link&utm_source=viewer)

<br>

![image (1)](https://github.com/LaFesta7/LikeFesta/assets/131599243/d1e9c0a0-24d3-42c2-b79b-4c68fad5c2d5)

#### 🚩 개발 기간
2023.08.16. ~ 2023.09.17.

<br>

## 2. 기술 스택 및 개발 환경

<details>
    
<summary>2.1. 서비스 아키텍처</summary>

<br>

![라페스타 서비스 아키텍처 최종 목표 수정 230913 drawio144](https://github.com/LaFesta7/LikeFesta/assets/131599243/f53cf90a-d4ab-4fbf-9e8b-e9e551ab790b)

</details>

<details>
    
<summary>2.2. 기술 스택</summary>

<br>

- Back-end : <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"><img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white"><img src="https://img.shields.io/badge/kakao login api (oauth2.0)-FFCD00?style=for-the-badge&logo=kakao&logoColor=white">
- Front-end : <img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white"><img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"><img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white"><img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"><img src="https://img.shields.io/badge/kakao map api-FFCD00?style=for-the-badge&logo=kakao&logoColor=white">
- Database : <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
- IDE : <img src="https://img.shields.io/badge/IntelliJ IDEA Ultimate-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
- SCM : <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
- TEST : <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"><img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
- CI/CD : <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"><img src="https://img.shields.io/badge/aws s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"><img src="https://img.shields.io/badge/aws ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"><img src="https://img.shields.io/badge/aws codedeploy-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"><img src="https://img.shields.io/badge/aws rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
- Communication : <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white"><img src="https://img.shields.io/badge/kakaotalk-FFCD00?style=for-the-badge&logo=kakaotalk&logoColor=white"><img src="https://img.shields.io/badge/gather-2560E0?style=for-the-badge&logo=&logoColor=white">
- Design : <img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"><img src="https://img.shields.io/badge/canva-00C4CC?style=for-the-badge&logo=canva&logoColor=white">
- ETC : <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">

</details>

<details>
    
<summary>2.3. 개발 환경</summary>

<br>

- JDK 17
- Spring Boot 3.1.2
- Spring dependency-management 1.1.2
- JUnit 5.9.3
- Gradle 8.2.1
- JJWT 0.11.5
- Swagger UI 4.15.5
- Spring Security 6.1.2

</details>

<br>

## 3. 설계

### 3.1. 최종 설계
<details>
<summary>3.1.1. Api 명세서</summary>
<br>
    
[→ 자세히 보러가기](https://documenter.getpostman.com/view/27924273/2s9YC7TBWf)

<br>

<img width="1018" alt="스크린샷 2023-09-18 01 유저" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/9422a755-5643-4bc6-97a4-539b949f4018">
<img width="1004" alt="스크린샷 2023-09-18 01-1 소셜로그인" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/669365ce-70e8-4ca6-9fb2-9f510c9a96b8">
<img width="1008" alt="스크린샷 2023-09-18 01-2 관리자기능" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/0ba45d6f-2e17-45d5-ba13-666d2d0a69b0">
<img width="1016" alt="스크린샷 2023-09-18 03 페스티벌" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/04a57c1d-a956-4bcc-8d9e-63ee59d84937">
<img width="1006" alt="스크린샷 2023-09-18 03-1 리뷰" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/1c93d56b-4c86-444f-9c2a-517e342280b0">
<img width="1026" alt="스크린샷 2023-09-18 03-2 댓글" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/20fc75c2-21e9-4ee4-91e2-bca79c029ac9">
<img width="1026" alt="스크린샷 2023-09-18 03-3 페스티벌게시요청" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/b64854d9-e7ba-425a-bdcb-c6ee4aaa9e98">
<img width="998" alt="스크린샷 2023-09-18 04 팔로우" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/24c34998-9176-481a-ac40-80b61b77e2b7">
<img width="1007" alt="스크린샷 2023-09-18 04-1 태그" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/8efaf7d3-38e0-40ce-8362-22615e5106b5">
<img width="994" alt="스크린샷 2023-09-18 04-2 뱃지" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/206760ea-74a3-4229-a6cc-a5506dc55512">
<img width="1003" alt="스크린샷 2023-09-18 05 알림" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/f001927f-0042-4039-8c97-d23952954b73">
<img width="1004" alt="스크린샷 2023-09-18 06-1 뷰" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/710f36cc-064f-4865-be4b-cf449a9ebda6">
<img width="1001" alt="스크린샷 2023-09-18 06-2 뷰" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/e9b962b4-ddfc-4d5b-aa81-ee9aaa0a806a">

</details>

<details>
<summary>3.2.2. ERD</summary>
    
![image](https://github.com/LaFesta7/LikeFesta/assets/131599243/fc0d34c7-e695-4c5d-8c9f-5bc65de4e414)

</details>

<br>

### 3.2. 초기 설계

<details>
<summary>3.2.1. Api 명세서</summary>

![a-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/4a4bb893-aa86-47d6-95e2-6a4f5fdc355f)
![b-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/20ae5c2b-695a-49c4-a9c2-1890303630bd)
![c](https://github.com/LaFesta7/LikeFesta/assets/132440453/d9bba998-0911-49a0-8f6c-1132bd9eeb86)
![d-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/9f9e516f-c55b-4c88-8f2c-3a45b7ef811b)
![e-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/1022aeae-7b85-46c2-a0a1-a2126617546e)
![f-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/eaa7a39e-2b6c-4ab2-8ed9-a81b64dfd575)
![g-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/fffea5ce-2d0c-453a-a5e9-6a21675b1bfc)
![h-2](https://github.com/LaFesta7/LikeFesta/assets/131860214/9fd797a5-645c-4116-b17a-d82ca36bac3e)

</details>

<details>
<summary>3.2.2. ERD</summary>
    
![초기 erd](https://github.com/LaFesta7/LikeFesta/assets/131599243/493be811-7dbe-4065-961a-3038b8fbb1f0)

</details>

<details>
<summary>3.2.3. 와이어프레임</summary>

![Group_1](https://github.com/LaFesta7/LikeFesta/assets/131860214/5522cba4-d879-4fdf-b41b-084fe7736bc1)
![Group_2](https://github.com/LaFesta7/LikeFesta/assets/131860214/b62477f5-4580-4fd0-93ad-899f8433483a)
![Group_3](https://github.com/LaFesta7/LikeFesta/assets/131860214/995e1e0e-e7b5-4d65-88fd-85f6c6db1d67)
![Group_4](https://github.com/LaFesta7/LikeFesta/assets/131860214/bc96e3f7-1aef-4eff-8a6d-4156d845727a)
![Group_5](https://github.com/LaFesta7/LikeFesta/assets/131860214/8945476f-e51f-4d20-994f-50fc8196ff30)
![Group_6](https://github.com/LaFesta7/LikeFesta/assets/131860214/ad4efa1c-d73f-4c1c-b030-b7d25b2c1167)
![Group_7](https://github.com/LaFesta7/LikeFesta/assets/131860214/267c3c3e-5f09-4ce6-86f8-11bd96942acd)
![Group_8](https://github.com/LaFesta7/LikeFesta/assets/131860214/43790f05-3d53-49e1-b09a-070407149777)

</details>

<br>

## 4. 기술적 의사 결정 및 트러블 슈팅

### 4.1. 기술적 의사 결정

<details>
    
<summary>4.1.1. GitHub Actions 을 이용한 CI / CD</summary>

* **도입 사유:** 협업을 진행함에 있어서, 테스트 및 배포를 수동으로 진행할 경우 수동으로 진행하는 일이므로 오류가 생길 수 있고, 개발 외에 일에 소모되는 시간이 상당히 발생하게 됩니다.

    <details>
        
    <summary>대안 탐색: GitHub Actions / Jenkins</summary>
    
    * **장-단점**
        - GitHub Actions
            - 장점 → GitHub와 연동이 원활하고, 무료로 제공한다.
            - 단점 → 비교적 신기술로 자료가 부족하고, 커스터마이징의 폭이 좁다.
        - Jenkins
            - 장점 → 커스터마이징의 폭이 넓고, 자료 찾기가 용이하다.
            - 단점 → 설정이 쉽지 않고, 보안 및 안정성 이슈가 발생할 수 있다.
    
    * **설치, 이용 방법**
        - GitHub Actions은 GitHub 저장소 내 워크플로우 파일 작성으로 설정이 가능합니다.
        - Jenkins는 별도의 서버와 플러그인의 설치가 필요합니다.
    
    * **지속, 통합 배포**
        - GitHub Actions과 Jenkins가 모두 지원합니다.
    
    * **사용언어**
        - GitHub Actions은 yaml을 사용하며
        - Jenkins는 java를 사용합니다.
     
    * **비용측면**
        - GitHub Actions은 무료 티어 범위를 가지며
        - Jenkins는 서버 유지 비용을 가지고 있습니다.
    
    </details>

* **최종 의사 결정:** 간단한 파일작성으로 설치 및 이용이 가능하고 비용적 측면에서 무료로 이용가능 할 수 있는 **GitHub Actions을 통해 CI/CD를 진행하기로 하였습니다.**

</details>

<details>
    
<summary>4.1.2. QueryDsl 도입</summary>

* 이전 코드는 JPA를 사용하여 복잡한 쿼리 작성에는 약점이 있었습니다. 보다 간략하고 정확한 코드를 위해 **QueryDsl을 도입**하였습니다.

-  **간략한 코드 작성**
    - 이전 코드에서는 JPA에서 조인을 하기 어려워 Service단계에서 한 번 더 Repository를 호출
    - QueryDsl을 도입한 이후 Repository를 한 번만 호출해도 조인된 데이터를 가져올 수 있게  됨
        - 보다 간략한 코드를 작성할 수 있게 됨
        - 향후 코드 수정이나 유지보수가 더 편리해질 것으로 예상됨
- **복잡한 Query문 작성 용이**
    - 이전에는 복잡한 Query문을 작성하기 위해 @Query를 사용
        - 문자열로 이루어져 있어 수정이 어려웠고, 오류가 있어도 찾기 힘들다는 단점이 있었음
    - QueryDsl은 Java로 작성하기 때문에 오류가 있다면 IDE가 알려줄 수 있음
        - 보다 편리하고 정확한 Query문 작성이 가능

</details>

<details>

<summary>4.1.3. Redis 도입</summary>

**Refresh Token을 통한 인증인가 구현**

1. Refresh Token을 구현하며 이를 통한 AccessToken 재발급 과정을 인메모리 DB를 통해 수행하고자 함.
2. 키값을 통해 바로 토큰정보를 get할 수 있어 O(1)로 가져올 수 있다.
3. 만료기간이 짧은 데이터, 각 1시간과 2주의 시간이 지나면 자동 삭제 가능

**회원가입 인증번호 임시 저장 DB**
 
1. DB가 유실되도 상대적으로 리스크가 적다.
2. 3분, 30분의 짧은 만료기간을 가졌기에 이를 Time to Live를 설정하여 이후 추가 삭제 쿼리를 날리지 않아도 된다.
3. 해당 가입정보 중 하나를 키값으로 설정해 인증번호를 get하는 속도를 O(1)로 가져갈 수 있다.

</details>

<details>

<summary>4.1.4. 알림 기능 실시간 통신</summary>

- 알림 기능에서 이전 코드는 사용자가 새로고침을 진행해야만 알림을 확인할 수 있습니다.
**사용자의 편의성을 높이기 위하여 알림 기능에 실시간 통신을 접목**하고자 합니다.

    <details>
    
    <summary>실시간 통신 방법</summary>
    
    - **Polling**
        - 주기적으로 서버에 요청을 보내 데이터 업데이트를 확인하는 방법
        - 서버의 부하를 낮추는 데 유용하지만, 실시간성은 상대적으로 낮을 수 있음
        - 따라서 데이터 업데이트가 빈번하지 않고 지연이 허용되는 경우에 적합
    - **Long-Polling**
        - 롱 폴링은 폴링의 확장 버전으로, 서버가 새 데이터를 가용할 때까지 응답을 보류
        - 롱 폴링은 실시간성을 향상시킬 수 있지만, 여전히 클라이언트와 서버 간에 더 많은 리소스를 사용
    - **SSE (Server-Sent Event)**
        - SSE는 클라이언트에서 서버로부터 데이터를 비동기적으로 수신하는 방법 중 하나
        - 특히 서버에서 클라이언트로 실시간 이벤트를 푸시할 때 유용
        - SSE는 단방향 통신이므로 클라이언트에서 서버로 데이터를 보내는 데는 Web Socket보다 제한적
    - **Web Socket**
        - Web Socket은 양방향 실시간 통신을 지원하는 풍부한 기능을 제공합
        - 클라이언트와 서버 간에 연결을 유지하고 언제든지 데이터를 교환할 수 있음
        - 따라서 실시간 채팅 애플리케이션 및 실시간 게임과 같이 양방향 통신이 필요한 시나리오에 적합합니다.
    
    </details>

- **결론**
    - 더 많은 실시간성과 양방향 통신이 필요한 경우 Web Socket을 고려할 수 있으나
    - 우리 프로젝트에서 알림 기능은 비교적 실시간성과 양방향성이 중요하지 않고 사용자의 편의성을 높이기 위한 실시간 통신이 필요한 것
    - 간단한 정보 업데이트 및 푸시 알림에는 SSE나 롱 폴링도 충분
    - 서버 최적화 측면에서 볼 때 SSE가 Long-Polling보다 더 효율적. 서버에서 클라이언트로 데이터를 푸시하는 방식이기 때문에, 연결 수가 많더라도 각 연결에 대한 부하가 낮고, 클라이언트와의 연결을 관리하는 데에도 부담이 적다.
    - **⇒ 프로젝트 적합성과 서버 최적화 측면을 고려하여 SSE 방법을 채택**
- **참고 블로그**
    - https://taemham.github.io/posts/Implementing_Notification/
    - https://tecoble.techcourse.co.kr/post/2022-10-11-server-sent-events/
    - [https://velog.io/@max9106/Spring-SSE-Server-Sent-Events를-이용한-실시간-알림](https://velog.io/@max9106/Spring-SSE-Server-Sent-Events%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%8B%A4%EC%8B%9C%EA%B0%84-%EC%95%8C%EB%A6%BC)

</details>

<br>

### 4.2. 트러블 슈팅

<details>

<summary>4.2.1. CD</summary>

1. 문제 발생
github actions 과정에서 node.js 12를 사용하여 오류 발생

    1-1 . 해결 방법

    ```jsx
    name: Set up Node.js
    uses: actions/setup-node@v2
    with:
    node-version: '16'
    ```

    위와 같은 코드를 cd 파일에 추가하여 기본적으로 node.js를 16으로 실행되도록 수정

2. 문제 발생
인스턴스 상태 검사 → 인스턴스 연결성 검사 통과하지 못하는 오류 발생

    2-1. 해결방법
   
        EC2에서 인스턴스의 모니터링을 해본 결과 CPU 사용률이 과도하게 높은 것을 발견
        서버를 재부팅했지만 마찬가지 상태여서 서버 중지 후 시작하는 방법으로 오류를
        해결

 4. 문제 발생
     jar 파일을 인식하지 못하는 문제 발생

    3-1. 해결방법
    
         s3로 파일이 이동되는지 확인, s3 속 파일을 다운로드 하여 파일구성을 확인
         start.jar 파일 속 cp `$PROJECT_ROOT`로 지정한 jar 파일의 위치를 정위치인
         `/build/libs/LaFesta-0.0.1-SNAPSHOT.jar $JAR_FILE`로 수정하고 재실행

</details>

<details>

<summary>4.2.2. CI 환경 설정 관련 오류</summary>

<details>

<summary>MySQl 설치 부분</summary>

- 발생한 예외(Githib Actions - build)
    
    ```
    [build](https://github.com/LaFesta7/LikeFesta/actions/runs/5951078786/job/16140180506#step:6:1)
    Unexpected input(s) 'host port', 'container port', 'character set server', 'collation server', valid inputs are ['entryPoint', 'args', 'mysql version', 'mysql database', 'mysql user', 'mysql password']
    
    ```
    
- 예외가 발생한 코드
    
    ```
    dev-ci.yml
    
    - name: MySQL 설치
            uses: samin/mysql-action@v1
            with:
              host port: 3306 # Optional, default value is 3306. The port of host
              container port: 3307 # Optional, default value is 3306. The port of container
              character set server: 'utf8' # Optional, default value is 'utf8mb4'. The '--character-set-server' option for mysqld
              collation server: 'utf8_general_ci' # Optional, default value is 'utf8mb4_general_ci'. The '--collation-server' option for mysqld
              mysql version: '8.0' # Optional, default value is "latest". The version of the MySQL
              mysql database: test # Optional, default value is "test". The specified database which will be create
              mysql user: developer # Required if "mysql root password" is empty, default is empty. The superuser for the specified database. Of course you can use secrets, too
              mysql password: ${{ secrets.DB_PASSWORD }}
    
    ```
    
- 원인 분석
    
    > GitHub Actions의 빌드 설정 파일에서 MySQL 관련 설정 부분에서 발생하는 문제. 현재 yml 파일에 MySQL 관련 설정이 포함되어 있지만, GitHub Actions에서 사용하는 actions/checkout@v3 액션은 기본적으로 컨테이너 환경 내에서 코드를 실행하므로, MySQL과 같은 데이터베이스 서버를 직접 설치하고 구성하는 것은 불필요 -> 외부 MYSQL 서비스 사용하는 방법으로 수정

- 수정한 코드
    
    ```java
    dev-ci.yml
    
    (위의 오류 코드 삭제 후 steps 위에 추가)
    
    services:
          mysql:
            image: mysql:latest
            env:
              MYSQL_ROOT_PASSWORD: root
              MYSQL_DATABASE: test_db
            ports:
              - 3306:3306
            options: --health-cmd="mysqladmin ping"
    ```
    
- [refactor] MYSQL 서비스가 테스트 환경에서만 외부에서 임시로 사용하는 서비스긴 하지만 정보가 숨겨져 있는것이 더 안전하다고 판단되어 해당 부분 환경변수로 수정
    
    ```java
    env:
          DB_URL: jdbc:mysql://localhost:3306/test_db
          DB_USER: root
          DB_PASSWORD: root
          DB_URL: ${{ secrets.DB_URL }}
          DB_USER: ${{ secrets.DB_USER }}
          DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
          JWT_SECRET_KEY: ${{ secrets.JWT_SECRET_KEY }}
          KAKAO_REST_API: ${{ secrets.KAKAO_REST_API }}
          MAIL_USERNAME: ${{ secrets.MAIL_USERNAME }}
          MAIL_PASSWORD: ${{ secrets.MAIL_PASSWORD }}
          MAIL_HOST: ${{ secrets.MAIL_HOST }}
          MAIL_PORT: ${{ secrets.MAIL_PORT }}
        services:
          mysql:
            image: mysql:latest
            env:
              MYSQL_ROOT_PASSWORD: root
              MYSQL_DATABASE: test_db
              MYSQL_ROOT_PASSWORD: ${{ secrets.DB_PASSWORD }}
              MYSQL_DATABASE: ${{ secrets.DB_DATABASE }}
            ports:
              - 3306:3306
            options: --health-cmd="mysqladmin ping"
    ```

</details>

<details>

<summary>Gradle with Build</summary>

- 발생한 예외(Githib Actions - build)
    
    ```
    java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@290807e5 testClass = com.sparta.lafesta.LaFestaApplicationTests, locations = [], classes = [com.sparta.lafesta.LaFestaApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceLocations = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@5143c662, org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5b1ebf56, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@4f25b795, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@4fad9bb2, org.springframework.boot.test.context.SpringBootTestAnnotation@c361b062], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]
    
    ```
    
- 예외가 발생한 코드
    
    ```
    dev-ci.yml
    
    ```
    
- 원인 분석
    
    > 다른 CI yml 파일들을 비교하다 보니 Gradle을 Build 하는 부분이 없어 문제가 생긴 것을 알게 됨
    
- 수정한 코드
    
    ```java
    dev-ci.yml
    
    (해당 코드 추가)
    
    - name: Build with Gradle
            uses: gradle/gradle-build-action@bd5760595778326ba7f1441bcf7e88b49de61a25 # v2.6.0
            with:
              arguments: build
    ```
    
- [refactor] 이전에 추가한 build with Gradle 단이 아래 있던 어플리케이션 실행 테스트 단으로 대체가 가능하다는 것을 알게되어 이전에 추가했던 필요하지 않은 build with Gradle은 삭제 진행함 → 위의 문제가 CI에 영향을 끼친 것은 아니라는 것을 알게됨

</details>

<details>

<summary>환경변수 설정 관련 오류</summary>

- 발생한 예외(Githib Actions - build)
    
    ```
    java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@290807e5 testClass = com.sparta.lafesta.LaFestaApplicationTests, locations = [], classes = [com.sparta.lafesta.LaFestaApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceLocations = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@5143c662, org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5b1ebf56, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@4f25b795, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@4fad9bb2, org.springframework.boot.test.context.SpringBootTestAnnotation@c361b062], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]
    
    ```
    
- 예외가 발생한 코드
    
    ```
    dev-ci.yml
    
    - name: yml 파일 생성
            run: |
              cd ./src/main/resources
              rm -rf ./application.properties
              touch ./application.yml
              echo "${{ secrets.APPLICATION_YML }}" > ./application.yml
              touch ./application-aws.yml
              echo "${{ secrets.APPLICATION_AWS_YML }}" > ./application-aws.yml
              touch ./application-key.yml
              echo "${{ secrets.APPLICATION_KEY_YML }}" > ./application-key.yml
            shell: bash
    
    ```
    
- 원인 분석
    
    > Github Actions secrets and variables 에서 환경변수를 설정하는 파일을 만들어 환경변수를 설정해야하지만 해당 부분을 잘 만들지 못해 오류가 발생
    
- 수정한 코드
    
    ```java
    dev-ci.yml
    
    (위의 코드 삭제 후 services 위에)
    
    env:
          DB_URL: jdbc:mysql://localhost:3306/test_db
          DB_USER: root
          DB_PASSWORD: root
          JWT_SECRET_KEY: ${{ secrets.JWT_SECRET_KEY }}
          KAKAO_REST_API: ${{ secrets.KAKAO_REST_API }}
    ```

</details>

</details>

<details>

<summary>4.2.3. 페스티벌 리마인더 알림 관련 이벤트 리스너에서 팔로워를 불러오지 못하는 문제 (Hibernate의 LazyLoading으로 인한 오류)</summary>

- 발생한 예외

```
// 런타임 Exception
org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.sparta.lafesta.user.entity.User.followers: could not initialize proxy - no Session
```

- 예외가 발생한 코드

```java
// FestivalCreatedEvent
    public FestivalCreatedEvent(Object source, Festival festival) {
        super(source);
        this.festival = festival;
    }
}

// FestivalCreatedEventPublisher
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishFestivalCreatedEvent(Festival festival) {
        log.info("이벤트 생성");
        FestivalCreatedEvent event = new FestivalCreatedEvent(this, festival);
        eventPublisher.publishEvent(event);
    }

// FestivalCreatedEventListener
    @Override
    @TransactionalEventListener
    public void onApplicationEvent(FestivalCreatedEvent event) {
        Festival festival = event.getFestival();
        User editor = festival.getUser();
        List<UserFollow> userFollows = editor.getFollowers();
        (생략)
```

- 원인 분석

> Hibernate의 Lazy Loading 기능을 사용할 때 발생하는 문제. 이 오류는 엔티티의 연관 관계를 지연로딩(Lazy Loading)으로 설정했을 때, 실제 데이터를 조회하지 않은 상태에서 연관된 컬렉션을 접근하려고 할 때 발생. -> festival에서 user를 갖고와 user에서 follower들을 가져오려고 하니 발생한 문제였음 -> 레포지토리에서 직접 가져오니 문제 해결

- 수정한 코드

```java
// FestivalCreatedEvent
    public FestivalCreatedEvent(Object source, Festival festival, List<User> followers) {
        super(source);
        this.festival = festival;
        this.followers = followers;
    }
}

// FestivalCreatedEventPublisher
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishFestivalCreatedEvent(Festival festival) {
        User editor = festival.getUser();
        List<User> followers = followService.findFollowers(editor);
        FestivalCreatedEvent event = new FestivalCreatedEvent(this, festival, followers);
        eventPublisher.publishEvent(event);
        log.info("페스티벌 작성 이벤트 생성");
    }

// FollowService 메소드 추가
    public List<User> findFollowers(User followedUser) {
        List<UserFollow> followUsers = userFollowRepository.findAllByFollowedUser(followedUser);
        List<User> followers = new ArrayList<>();
        for (UserFollow follower : followUsers) {
            User followerUser = userRepository.findByFollowers(follower).orElse(null);
            followers.add(followerUser);
        }
        return followers;
    }

// FestivalCreatedListener
    @Override
    @TransactionalEventListener
    public void onApplicationEvent(FestivalCreatedEvent event) {
        Festival festival = event.getFestival();
        (생략)
        List<User> followers = event.getFollowers();
        (생략)
    }
```

</details>

<details>

<summary>4.2.4. 칼럼명 read 예약어로 인해 Notification Entity 테이블이 생성되지 않는 문제 (컬럼명 예약어로 인한 오류)</summary>

- 발생한 예외

```
// 런타임 Exception
org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL "
    create table notifications (
        id bigint not null auto_increment,
        created_at datetime(6) not null,
        editor varchar(255) not null,
        expiration_time datetime(6) not null,
        read bit not null,
        title varchar(255) not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB" via JDBC [You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'read bit not null,
        title varchar(255) not null,
        user_id bigint' at line 6]
```

- 예외가 발생한 코드

```java
// Notification (Entity)
    @Column(name = "read", nullable = false)
    private Boolean read;
```

- 원인 분석

> read는 MySQL에서 예약어로 사용되는 키워드 중 하나이므로 컬럼 이름으로 사용하기에는 적합하지 않다. MySQL에서 키워드를 컬럼 이름으로 사용하려면 백틱(`)으로 묶어주어야 한다. -> 백틱으로 묶어주어 해결 -> 컬럼 이름으로 사용이 적합하지 않다하여 '변수명 짓기' 사이트 참고하여 컬럼명 재작성
> 
- 수정한 코드

```java
// Notification (Entity)
@Column(name = "`read`", nullable = false)
private Boolean read;
```

- 재수정한 코드

```java
// Notification (Entity)
@Column(name = "rd", nullable = false)
private Boolean rd;
```

</details>

<details>

<summary>4.2.5. 뱃지 태그와 페스티벌 태그를 비교해 공통된 경우를 찾아 카운트하는 로직에서 카운트하지 못하는 문제 발생 (연관관게가 복잡해짐에 따라 이해가 어려워 발생한 오류)</summary>

- 발생한 예외
    - 매칭 카운터가 4가 되야하는 상황에서 0인 상태로 디버깅 됨

!https://user-images.githubusercontent.com/131599243/264527353-70b43d86-0835-4f45-a54a-4184391a8a43.png

- 예외가 발생한 코드

```java
// BadgeServiceImpl

// 태그와 연관지어 장르별 빈도 수에 따른 뱃지 추가
    @Transactional
    public void checkBadgeTagFrequency(User user, Badge badge, List<Review> reviews, LocalDateTime startDay, LocalDateTime endDay) {
        List<Festival> festivals = festivalRepository.findAllByOpenDateBetween(startDay, endDay);
        List<Tag> tags = badge.getBadgeTags().stream().map(BadgeTag::getTag).toList();

        long matchingFestivalCount = festivals.stream()
                .filter(festival -> reviews.stream()
                        .allMatch(review -> review.getFestival().equals(festival)
                                && festival.getTags().containsAll(tags)))
                .count();

        if (matchingFestivalCount >= badge.getConditionStandard()) {
            createUserBadge(user, badge);
        }
    }
```

- 원인 분석

> 뱃지태그에서 태그를 불러올 때 생기는 문제라고 생각해 뱃지 태그 엔티티에서 태그 fethType을 EAGER로 변경-> 태그 객체의 주소는 잘 비교하므로 해당 문제는 아니였음festival.getTags()를 변수명때문에 List를 가져온다고 착각해서 문제가 발생 -> List를 가져오는데 비교 대상인 tags는 List이므로 당연히 비교가 불가 -> 수정 후 여전히 카운트 안되는 상태stream 구문이 잘못된 건가 싶어 stream을 for/if 문 등으로 풀어서 작성해봄풀어보니 containsAll() 메소드를 잘못 불러왔다는 것을 깨닫게 됨완성) 페스티벌 태그와 뱃지 태그를 비교할 때 페스티벌 태그 요소 중 뱃지 태그의 요소가 하나라도 있으면 카운트 해야되는 상황이므로 festivalTags.stream().anyMatch(badgeTags::contains) 로 조건을 수정하니 해결 완료
> 
- 수정한 코드

```java
// BadgeServiceImpl

// 태그와 연관지어 장르별 빈도 수에 따른 뱃지 추가
    @Transactional
    public void checkBadgeTagFrequency(User user, Badge badge, List<Review> reviews, LocalDateTime startDay, LocalDateTime endDay) {
        List<Festival> festivals = festivalRepository.findAllByOpenDateBetween(startDay, endDay);
        List<Tag> badgeTags = badge.getBadgeTags().stream().map(BadgeTag::getTag).toList();

        int matchingFestivalCount = 0;
        for (Festival festival : festivals) {
            List<Tag> festivalTags = festival.getTags().stream().map(FestivalTag::getTag).toList();
            for (Review review : reviews) {
                if (review.getFestival().equals(festival)) {
                    if (festivalTags.stream().anyMatch(badgeTags::contains)) {
                        matchingFestivalCount++;
                    }
                }
            }
        }

        if (matchingFestivalCount >= badge.getConditionStandard()) {
            createUserBadge(user, badge);
        }
    }
```

</details>

<details>

<summary>4.2.6. User 조회/수정/삭제 시 로그인 한 유저의 정보를 가져오지 못하는 문제 발생 (Repository 접근 오류)</summary>

- 발생한 오류

```
// 포스트맨
작성한 대로 Response되지 않고 HTML 파일을 가져오는 문제 발생
```

- 예외가 발생한 코드

```java
// UserService
    @Transactional(readOnly = true)
    public UserInfoResponseDto selectUserInfo(User user) {
        return new UserInfoResponseDto(user);
    }
```

- 원인 분석

> Service에 받아온 User는 UserDetailsImpl에서 받아온 User 정보로 UserRepository에 접근되지 않은 정보이다. 따라서 user의 DB에 존재하는 정보를 조회하거나 수정,삭제할 수 없다! -> UserDetailsImpl에서 받아온 User의 Id로 UserRepository에서 User 정보를 DB에서 받아와 조회/수정하면 해결 완료!
> 

- 수정한 코드

```java
// UserService
    @Transactional(readOnly = true)
    public UserInfoResponseDto selectUserInfo(User user) {
        User selectUser = findUser(user.getId());
        return new UserInfoResponseDto(selectUser);
    }
```

</details>

<br>

### 4.3. 사용성 테스트 및 유저 피드백에 따른 리팩토링 및 트러블 슈팅
<details>

<summary>4.3.1. 프론트에서 페스티벌, 리뷰, 유저 상세 조회 시 데이터가 조회되지 않는 문제 발생</summary>

* 발생한 예외
<img width="1278" alt="스크린샷 2023-09-17 194308" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/53c2e77e-decb-4625-a2f8-0dbc6bb1b196">
<img width="1275" alt="스크린샷 2023-09-17 194546" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/1fd812a0-5397-46b4-abcf-2f3670e0e256">
<img width="1272" alt="스크린샷 2023-09-17 194641" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/7976bd48-45f7-4ff3-87fe-d8939b84c9a0">


* 예외가 발생한 코드 (페스티벌을 예시로 가져옴)

```java
// FestivalResponseDto.java
private List<FileOnS3Dto> files;

this.files = festival.getFestivalFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();

// festival.js
<img src="${data.files[0].uploadFileUrl}" alt="축제 이미지" class="festival-image">
```

* 원인 분석

> 이미지가 업로드 되지 않고 데이터가 들어갈 경우, 해당 데이터에 대한 값이 없어 js에서 해당 값을 불러오지 못하고 오류 발생 -> 전체 데이터가 조회 되지 않는 문제 발생 -> 이미지 데이터가 존재하지 않을 경우에 프론트에서 대체 이미지 설정하여 해결

* 수정한 코드

```javascript
// festival.js
<img src="${data.files[0] ? data.files[0].uploadFileUrl : '/images/background/img-21.jpg'}" alt="축제 이미지" class="festival-image">
```

* 추가 수정 사항

> 프론트 상 로직은 최대한 간결하게 하기 위하여 대체 이미지 설정을 프론트에서 하지 않고 백에서 대체 이미지를 설정 후 프론트에 넘겨주도록 코드 재수정

* 재수정한 코드

```java
// FestivalResponseDto.java
private List<FileOnS3Dto> files;
private String fileUrl;

this.files = festival.getFestivalFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
this.fileUrl = files.size() > 0 ? files.get(0).getUploadFileUrl() : "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FxGuK9%2FbtsufX7IOe1%2FdJbJpCZ5UM6CYK5vGkS8Tk%2Fimg.png";

// festival.js
<img src="${data.fileUrl}" alt="축제 이미지" class="festival-image">
```

* 해결 후 화면(데이터가 조회되며 대체 이미지가 보여짐)
<img width="1263" alt="스크린샷 2023-09-17 202540" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/04a8f731-9b5b-4b8b-baf1-d963a7bfd267">
<img width="1276" alt="스크린샷 2023-09-17 202556" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/4d4b1bac-ca82-4dae-9f9d-a95d11d4ffed">
<img width="1279" alt="스크린샷 2023-09-17 200842" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/920b1d0c-02cd-4e09-9330-305555fa6b10">

</details>

<details>

<summary>4.3.2. 유저 테스트 중 xss 공격 데이터 발생 -> 해당 공격에 대비한 코드 수정 필요</summary>

* 발생한 예외(alert가 뜨며 내용이 보이지 않음)
<img width="812" alt="스크린샷 2023-09-17 202902" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/eddcccce-333f-4dc6-bbe3-a93a65ed8bb9">
<img width="1275" alt="스크린샷 2023-09-17 202657" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/1d18d817-92ef-4fb9-9dfa-1ce9d01fd762">
<img width="1279" alt="스크린샷 2023-09-17 202732" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/235cf31b-ad15-4322-a1e9-aaf0580d18b5">

* 예외가 발생한 코드
```java
// ReviewResponseDto.java
    private String content;
    
        this.content = review.getContent();
```

* 원인 분석

> xss(크로스사이트 스크립트) 공격으로 인한 문제였다. 해당 문제에 대해 공부 후 String Response를 안전한 코드로 변환하여 return할 수 있도록 공격이 예상되는 ResponseDto에 StringFormatter를 모두 추가함

> xss? 웹 페이지에 악의적인 스크립트를 포함시켜 사용자 측에서 실행되게 유도할 수 있다. 예를 들어, 검증되지 않은 외부 입력이 동적 웹페이지 생성에 사용될 경우, 전송된 동적 웹페이지를 열람하는 접속자의 권한으로 부적절한 스크립트가 수행되어 정보유출 등의 공격을 유발할 수 있다.

> xss에 대한 보안대책? 외부 입력값 또는 출력값에 스크립트가 삽입되지 못하도록 문자열 치환 함수를 사용하여 & < > " ' /( ) 등을 &amp; &lt; &gt; &quot; &#x27; &#x2F; &#x28; &#x29;로 치환하거나, JSTL 또는 잘 알려진 크로스 사이트 스크립트 방지 라이브러리를 활용한다. HTML 태그를 허용하는 게시판에서는 허용되는 HTML 태그들을 화이트리스트로 만들어 해당 태그만 지원하도록 한다.

> 참고 링크: https://www.kisa.or.kr/2060204/form?postSeq=5&lang_type=KO&page=1#fnPostAttachDownload

* 수정한 코드

```java
// StringFormatter.java 추가
public class StringFormatter {
    public static String format(String requestString) {
        requestString = requestString.replaceAll("&", "&amp;");
        requestString = requestString.replaceAll("<", "&lt;");
        requestString = requestString.replaceAll(">", "&gt;");
        requestString = requestString.replaceAll("￦", "&quot;");
        requestString = requestString.replaceAll("'", "&#x27;");
        requestString = requestString.replaceAll("/", "&#x2F;");
        requestString = requestString.replaceAll("\\(", "&#x28;");
        requestString = requestString.replaceAll("\\)", "&#x29;");
        return requestString;
    }
}

// ReviewResponseDto.java (리뷰뿐만 아니라 공격이 예상되는 String값에 모두 StringFormatter 도입)
    private String title;
    private String content;

        this.title = StringFormatter.format(review.getTitle());
        this.content = StringFormatter.format(review.getContent());
```

* 해결 후 화면
<img width="1259" alt="스크린샷 2023-09-17 212807" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/9a23ebbb-a269-423d-ba47-3a9459cf2c1d">
<img width="1269" alt="스크린샷 2023-09-17 212819" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/ee939d6c-04bd-49a6-8070-b7b9383b877b">
<img width="1280" alt="스크린샷 2023-09-17 212903" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/246cc300-1a76-4869-98ae-86269cca465d">
<img width="1280" alt="스크린샷 2023-09-17 213157" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/8951656d-a7a9-4ec5-b0f0-688ecb7464f4">
<img width="1280" alt="스크린샷 2023-09-17 213300" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/c5e1bb3a-5eeb-44a3-bf27-f3c23a5e82f2">
<img width="1274" alt="스크린샷 2023-09-17 213325" src="https://github.com/LaFesta7/LikeFesta/assets/131599243/c8bf2c43-83ff-42b8-b5e5-bd03bfebe560">

</details>

<br>

## 5. 팀원 소개 및 역할 분담

#### [권진혁(리더)](https://github.com/05030522)

* Back: User 인증인가 및 회원가입/로그인, 소셜 로그인, 페스티벌 라인업, CI/CD 구축 및 도메인 설정
* Front: 카카오맵 오픈 API 적용, 정적페이지 작성, 백 연결 1차
* Etc.: READ ME 1차 작성, 기술버전 기록, 발표 대본 및 자료(PPT) 준비, 중간발표 시연영상 제작, 최종 1차 발표 영상 제작, 중간 발표 및 최종 발표

#### [김현우(부리더)](https://github.com/Wooin-dev)

* Back: 첨부파일(S3), 테스트 코드 방법 서칭, Redis(클라우드)-> 리프레시 토큰/이메일 인증코드
* Etc.: 와이어프레임 작성, 기술버전 기록, 원페이지 노션(협력사 전송 브로셔 소스) 작성

#### [조해나(팀원)](https://github.com/HaenaCho01)
* Back: 페스티벌-리뷰-코멘트-페스티벌 게시 요청 기본 CRUD, User 조회/수정/탈퇴, User 기능 오류 해결 및 개선, 좋아요, 권한 나누기, 알림(이메일/실시간 웹), 뱃지, 사용성 테스트 피드백 반영 리팩토링, CI/CD 환경 설정
* Front: 카카오맵 오픈 API 조회 연결, 추가 정적페이지 작성, 백 연결 2차 및 최종
* Etc.: 서비스 아키텍처 작성, READ ME 최종 작성, 최종발표 시연영상 제작

#### [정해인(팀원)](https://github.com/haeinjung3)
* Back: 팔로우, 회원가입 이메일 인증, 페이징 처리, 태그 및 태그별 조회, 랭킹, QueryDSL 및 키워드 검색
* Front: 페이징 처리
* Etc.: READ ME 2차 작성, 고객 피드백 구글 설문 폼 작성, 원페이지 노션(협력사 전송 브로셔 소스) 작성

#### [정지상(팀원)](https://github.com/jjsjjs9)
* Back: User 조회/수정/탈퇴 시도
