# BudgetMate
사용자가 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 서비스로 사용자들이 예산을 설정하고 지출을 모니터링하며 목표를 달성하는 데 도움을 주는 것을 목표로 합니다!
</br>

## 목차
- [개발 기간](#개발-기간)
- [개요](#개요)
- [기술 스택](#기술-스택)
- [데이터베이스 모델링](#데이터베이스-모델링)
- [API 명세](#API-명세)
- [구현 기능](#구현-기능)
- [트러블 슈팅](#트러블-슈팅)
- [이슈 관리](#이슈-관리)
<br/>

## 개발 기간
2024.09.12 - 2024.09.20
<br/>

## 개요
**예산 설정 및 설계 서비스**
- `월별` 총 예산을 설정합니다.
- `카테고리` 별 예산을 설계(=추천)하여 사용자의 과다 지출을 방지합니다.
  
**지출 기록**
- 사용자는 `지출` 을  `금액`, `카테고리` 등을 지정하여 등록 합니다. 언제든지 수정 및 삭제 할 수 있습니다.
  
**지출 컨설팅**
- `월별` 설정한 예산을 기준으로 오늘 소비 가능한 `지출` 을 알려줍니다.
- 매일 발생한 `지출` 을 `카테고리` 별로 안내받습니다.

**지출 통계**
- `지난 달 대비` , `지난 요일 대비`,  `다른 유저 대비` 등 여러 기준 `카테고리 별` 지출 통계를 확인 할 수 있습니다.
  
<br/>

## 기술 스택
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-17-blue) ![Static Badge](https://img.shields.io/badge/Springboot-3.2.8-red)<br/>
데이터 베이스: ![Static Badge](https://img.shields.io/badge/Mysql-8.0.1-blue) <br/>
배포 : ![Static Badge](https://img.shields.io/badge/Docker-039BC6) ![Static Badge](https://img.shields.io/badge/AWS-EC2-orange) <br/> ETC : ![Static Badge](https://img.shields.io/badge/Redis-red)

<br/>

## 데이터베이스 모델링
<img width="892" alt="image" src="https://github.com/user-attachments/assets/f7216a07-8f35-400f-b1d9-0ed5e57cbc32">
<br/>

## API 명세
| **분류** | **API 명칭** | **HTTP 메서드** | **엔드포인트** | **설명** |
| --- | --- | --- | --- | --- |
| **인증&인가** | 사용자 회원가입 | POST | /api/user/signup | 사용자는 이메일, 비밀번호로 회원가입합니다. |
|  | 사용자 로그인 | POST | /api/user/login | 사용자는 계정명, 비밀번호로 로그인합니다. 로그인 시 ReponseBody에 Refresh Token, Access Token이 발급됩니다. |
|  | 사용자 로그아웃  | POST | /api/user/logout | 사용자는 로그아웃합니다.   로그아웃 시 Redis에 AcessToken을 저장합니다. |
|  | 토큰 재발급 | POST | /api/user/reissue | 유효한 Refresh Token으로 Access Token, Refresh Token을 재발급합니다. |
| **카테고리** | 카테고리 조회 | GET | category/list | 카테고리 목록을 조회합니다. |
| **예산** | 카테고리별 예산 생성 | POST | /api/category-budget | 카테고리 별 예산을 생성합니다. |
|  | 카테고리별 예산 수정 | PUT | /api/category-budget/{id} | 카테고리별 예산을 수정합니다. |
| **지출** | 지출 생성 | POST | /api/expenses | 지출을 생성합니다. |
|  | 지출 수정 | PUT | /api/expenses/1 | 지출을 수정합니다. |
|  | 지출 삭제 | DELETE | /api/expenses/1 | 지출을 삭제합니다. |

<br/>

## 구현 기능
**인증&인가**
- 사용자 회원가입
  + 사용자는 이메일, 비밀번호로 회원가입합니다.
- 사용자 로그인
  + 로그인 시 ReponseBody에 Refresh Token, Access Token이 발급됩니다.
- 사용자 로그아웃
  + 로그아웃 시 Redis에 AcessToken을 저장합니다.
- 토큰 재발급
  + 유효한 Refresh Token으로 Access Token, Refresh Token을 재발급합니다.
  
**카테고리**
- 카테고리 목록을 조회합니다.
  
**예산**
- 카테고리별 예산 생성	및 수정
  
**지출**
- 지출 생성, 수정, 삭제

<br/>


## 트러블 슈팅


<br/>

## 이슈 관리


<br/>

