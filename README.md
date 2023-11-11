# notice api

### 구현방법
```
Java, Spring Boot, Gradle, JPA, querydsl, H2 database
```

## 실행방법
* github pull 이후, 빌드 한 뒤 메인 메서드(api 모듈의 notice 패키지의 Application.java에 위치) 실행
* swagger uri(swagger-ui/index.html)

## 구현기능
![캡처](https://github.com/jaebum7396/aswe/assets/38182229/56e57b0c-8fb1-4ff6-8a91-5e1905636226)
* 일부 요청에는 요청 헤더에 Authorization이 필요합니다!(-H Authorization : ${jwt} -- Bearer 없이 token만!)


