# Exchange-oauth

Spring Boot를 이용한 트레이딩 입출금 서버 제작

## Developer

- 👩‍💻 [HashCitrine](https://github.com/HashCitrine) (`webflux, oauth, MSA` 구현 및 개발 흐름 기획)
- 👩‍💻 [Soo-ss](https://github.com/Soo-ss) (`api, oauth, wallet` 구현)

## 사용 기술

- Spring Boot
- PostgreSQL
- Kafka
- Lombok

※ Kafka는 Docker container 이용.

## 개발한 서버 목록

- exchange-api
- exchange-oauth
- exchange-eureka
- exchange-gateway
- exchange-webflux
- exchange-wallet

😀 `exchange-api, exchange-oauth, exchange-wallet, exchange-webflux` 4개의 서버를 관리하기 위해, `exchange-eureka, exchange-gateway`를 통하여 MSA를 구현했습니다.

## 토큰 기반 서비스 이용자 인증

---

### 1. Oauth 인증이 필요한 서비스 목록  
- `exchange-api` : 거래금 충전(/charge), 거래 요청(/order)  
- `exchange-webflux` : 거래 처리(submitOrder)  
- `exchange-wallet` : 입출금 처리(submitDw)

---

### 2. 전체 서비스 흐름
1. exchange-api를 통해 이용자 등록(/resiter)
2. exchange-oauth를 통해 서비스 이용을 위한 토큰 발급 (/token)
3. exchange-api를 통해 발급 받은 토큰과 함께 서비스 요청(거래금  충전 : /charge, 거래 요청 : /order)
4. exchange-oauth에서 토큰 검증 후 실제 처리 로직이 있는 서비스에 kafka를 통해 요청
- 거래금 충전 : `submitDw` ► `exchange-wallet`에서 처리
- 거래 요청 : `submit` ► `exchange-webflux`에서 처리

---

## Class 역할 정리
1. `OauthController` : 서비스에 등록된 이용자가 서비스 이용을 위해 토큰 발급 요청을 받기 위한 컨트롤러 (**/token**)
2.  `OauthConsumer` : excange-api에서 요청 받은 거래금 충전(reqDw), 거래 요청(reqOrder)을 입수 및 토큰 검증 성공 후 실제 요청 처리 서비스로 전달
3. `authCheck` : 요청과 함께 전달된 토큰값 검증
4. `JwtAndPassword` : 토큰 생성 및 검증을 위한 util
5. `BankStatementService, OrderService` : 토큰 검증 성공 시 이용자 요청 처리 상태를 DB에 업데이트(**updateStatus**) 
