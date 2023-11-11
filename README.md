# notice api

### 구현방법
```
Java, Spring Boot, Gradle, JPA, querydsl, H2 database
```

### 프로젝트 구조
![캡처](https://github.com/jaebum7396/notice/assets/38182229/8e46344b-cc32-409d-8b72-34eb6040e9a2)  
root project인 notice 아래에 실제 기능 구현부인 api 모듈이 위치하고  
횡단관심사를 관리하기 위한 common 모듈을 서브모듈 (https://github.com/jaebum7396/common) 로 가지고 있습니다 .

### 실행방법
* github pull 이후, 빌드 한 뒤 메인 메서드(api 모듈의 notice 패키지의 Application.java에 위치) 실행
* swagger uri(swagger-ui/index.html)

### 구현기능
![캡처](https://github.com/jaebum7396/notice/assets/38182229/70d21cd8-fd11-4ce3-8367-e73457dee56a)
* 일부 요청에는 요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt})
* test token -- eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJObSI6ImFkbWluIn0.ELfEy-gNZsxrwKCq5VAN2dQRFHCtOjiksszDMfU5ai4
  
### 요청예시

#### create -- 공지사항 작성 (요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt}))
![캡처](https://github.com/jaebum7396/notice/assets/38182229/aec6d7cc-f0d5-4a82-828b-80776da03f11)

#### read -- 공지사항 조회 (읽을 시 views +1)
![캡처](https://github.com/jaebum7396/notice/assets/38182229/c8213e4f-0fb8-41e9-9b87-4f9cd4dbfeb6)

#### put -- 공지사항 수정 (요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt}))
![캡처](https://github.com/jaebum7396/notice/assets/38182229/42149ea2-8905-45b5-8819-998962412f21)

#### delete -- 공지사항 삭제 (요청 헤더에 Authorization이 필요합니다!(-H Authorization : Bearer ${jwt}))
delete의 경우 물리 삭제가 아닌 논리 삭제(deleteYn 필드값 활용)로 구현하였습니다.
![캡처](https://github.com/jaebum7396/notice/assets/38182229/62c7451b-03eb-42c6-8ee8-5bdfbc9e7f0d)



