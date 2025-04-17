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
  - https://github.com/pranludi/crossfit-gateway
- Member
  - 회원 가입, 조회 처리, ...
  - https://github.com/pranludi/crossfit-member
- Branch
  - 지점 가입, 조회 처리, ...
  - https://github.com/pranludi/crossfit-branch
- Protocol
  - protobuf 파일들
  - git submodule add git@github.com:pranludi/crossfit-protocol.git

### 대략 구성
![crossfit-service.png](docs/crossfit-service.png)

### todo ...
~~1. [protobuf 파일 submodule(repository)](https://github.com/pranludi/crossfit-gateway/issues/2)~~
2. [grpc response 구조 변경( circuit breaker 대응 용 )](https://github.com/pranludi/crossfit-gateway/pull/7)
3. [eureka](https://github.com/pranludi/crossfit-gateway/pull/10)
4. [local, qa, prod 환경 구분 및 DB 분리](https://github.com/pranludi/crossfit-gateway/pull/8)
5. [테스트 추가](https://github.com/pranludi/crossfit-gateway/pull/9)
6. [docker 기반으로 테스트 할 수 있도록 docker compose 구성](https://github.com/pranludi/crossfit-gateway/pull/11)
7. [docker 에서 발생하는 로그 수집 및 kafka 으로 전달하도록 구성](https://github.com/pranludi/crossfit-gateway/pull/12)
8. [kafka stream 을 이용해서 실시간 로그 분석 ?](https://github.com/pranludi/crossfit-gateway/pull/15)
9. [elasticsearch(opensearch) / kibana ?](https://github.com/pranludi/crossfit-gateway/pull/16)
10. [member 와 branch 의 로직 추가 ?](https://github.com/pranludi/crossfit-gateway/pull/13)
11. [board 게시판 서비스 추가 ?](https://github.com/pranludi/crossfit-gateway/pull/14)
