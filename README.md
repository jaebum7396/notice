# notice api

### 구현방법
```
Java, Spring Boot, Gradle, JPA, querydsl, H2 database
```

### 프로젝트 구조
![캡처](https://github.com/jaebum7396/notice/assets/38182229/0bee95a5-af5b-4191-b498-f02a7b95cd4a)
root project인 notice 아래에 실제 기능 구현부인 api 모듈이 위치하고  
횡단관심사를 관리하기 위한 common 모듈을 서브모듈 (https://github.com/jaebum7396/common) 로 가지고 있습니다 .

### 실행방법
* github pull 이후, 빌드 한 뒤 메인 메서드(api 모듈의 notice 패키지의 Application.java에 위치) 실행
* test code 위치 -- api 모듈의 test 모듈에 위치(controller, service 의 layer 별로 작성)
* swagger uri(swagger-ui/index.html)
* h2 database를 inmemory로 실행하고 있습니다.(별도 설정 불필요 -- 서버모드로 실행하기 위해서는 별도로 h2 설치가 필요합니다.)

### 구현기능
![캡처](https://github.com/jaebum7396/notice/assets/38182229/5980a332-4184-4c37-a98e-864ea03a787a)
* 일부 요청에는 요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt})
* test token -- eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJObSI6ImFkbWluIn0.ELfEy-gNZsxrwKCq5VAN2dQRFHCtOjiksszDMfU5ai4
  
### 요청예시

#### create -- 공지사항 작성 (요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt}))
![캡처](https://github.com/jaebum7396/notice/assets/38182229/bd745f56-1b5c-4c2c-8d5b-125b5e58b52a)
#### read -- 공지사항 조회 (읽을 시 views +1)
![캡처](https://github.com/jaebum7396/notice/assets/38182229/fab65ada-8d7a-4188-8c9a-3ac386442690)

#### put -- 공지사항 수정 (요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt}))
![캡처](https://github.com/jaebum7396/notice/assets/38182229/dd4ac7d0-d5c2-4550-8ebd-bf3b35417ea9)

#### delete -- 공지사항 삭제 (요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt}))
delete의 경우 물리 삭제가 아닌 논리 삭제(deleteYn 필드값 활용)로 구현하였습니다.
![캡처](https://github.com/jaebum7396/notice/assets/38182229/145216c3-8f37-4762-a532-755a10074296)



