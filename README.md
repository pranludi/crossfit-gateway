# Crossfit 서비스

### 기본
- Java 21
- Spring Boot
- Kafka
- H2
- gRPC
- JWT
- MapStruct
- vavr

### MSA 형태로 구현
- Gateway
  - 요청을 받아서 각 서비스에게 처리 요청 및 집계 처리
- Member
  - 회원 가입, 조회 처리, ...
- Branch
  - 지점 가입, 조회 처리, ...

### 대략 구성
![crossfit-service.png](docs/crossfit-service.png)
