<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-service">
</p>

<br>

# 인프라공방 샘플 서비스 - 지하철 노선도

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew clean build
```
<br>


### 1단계 - 웹 성능 테스트
1. 웹 성능예산은 어느정도가 적당하다고 생각하시나요

- 네이버지도와 카카오맵은 지하철을 제외하고도 다양한 서비스를 포함하고 있어서 경쟁 사이트에서 제외. 서울교통공사와 비교
- pagespeed에서 성능을 측정한 결과 서울교통공사의 성능 82, subway의 성능 68로 성능 부분에서 가장 큰 차이를 보임

| 항목  |subway|서울교통공사|
|-----|---|---|
|FCP|2.7s|1.5s|
|SI|2.7s|2.0s|
|LCP|2.8s|1.8s|
|TTI|2.8s|1.9s|

- 주로 정적 콘텐츠를 응답하는 부분에서 느린 것으로 확인됨. 위의 항목에 대해 서울교통공사의 정적 콘텐츠 서빙 속도를 목표 기준으로 삼는게 좋아보임
  - 텍스트 압축 사용, 사용하지 않는 자바스크립트 줄이기, 효율적인 캐시 정책 사용

```
# 정량 기준
- 텍스트 압축을 통해 자바스크립트 파일의 크기가 1MB를 넘지 않아야 한다.
- 사용하지 않는 자바스크립트 파일은 지연로딩 설정한다.

# 시간 기준
- FCP 1.8초 미만
- SI 2.3초 미만
- LCP 2.1초 미만
- TTI 2.2초 미만

# 규칙 기준
- pagespeed 기준으로 성능 지표가 80점을 넘겨야 한다.
- 서울교통공사의 성능 지표인 82점을 넘겨야 한다.
```


2. 웹 성능예산을 바탕으로 현재 지하철 노선도 서비스의 서버 목표 응답시간 가설을 세워보세요.

- 서버에서 모든 응답을 TTI 기준인 2.2초 내로 할 수 있어야 한다.
  - 최번시인 08-09시, 18-19시에 대략 100만 명이 지하철을 이용한다.
  - 최번시에는 약 10만 명의 사용자가 이용을 해도 문제 없이 요청을 처리해야 한다.
---

### 2단계 - 부하 테스트 
1. 부하테스트 전제조건은 어느정도로 설정하셨나요

- 길찾기를 위해 사용자가 가장 많은 요청을 할 것으로 예상되는 경로
  - `GET /paths/?source={sourceNum}&target={targetNum}`, `GET /stations`
- 최번시(08-09, 18-19)를 기준으로 1시간 동안 10만 명의 사용자가 해당 요청을 최소 한 번 이상 보내는 것으로 범위 설정
- 목표 TTI 기준인 2.2초 내로 모든 응답을 처리할 수 있어야 함

```bash
# 웹 성능 개선된 목록
- 정적 파일 gzip 압축 적용
- HTTP cache 적용
- js 파일 비동기 로딩 적용

# WAS
- 동기 처리 코드 제거
- 무한 병렬 처리 코드 제거
```

- 목표 rps

```
1일 사용자 수(50만) * 1명당 1일 평균 접속 수(2회) = 1일 총 접속수(100만)
1일 총 접속수(100만) / 86400(60*60*24, 초/일) = 1일 평균 rps(약 12rps)
1일 평균 rps(12rps) * (최대 트래픽 / 평소 트래픽) = 약 60 rps
```

- vuser

```
R = 3
http_req_duration = 2.2
T = (R * http_req_duration)
T = 6.6
VUser = (목표 rps * T) / R

# 평균
# 12 rps
vu = 26

# 최번시
# 60 rps
vu = 132
```

3. Smoke, Load, Stress 테스트 스크립트와 결과를 공유해주세요.

- 시나리오: 인덱스 페이지 접속 -> 경로 검색 페이지 이동 -> 경로 검색
- load 디렉토리 내부에 스크립트와 결과물이 있습니다.

---

### 3단계 - 로깅, 모니터링
1. 각 서버내 로깅 경로를 알려주세요

- public b
  - /home/ubuntu/logs/subway/file.log
  - /home/ubuntu/logs/subway/json.log
  - /home/ubuntu/logs/subway/error.log
  - /var/log/nginx/access.log
  - /var/log/nginx/error.log

3. Cloudwatch 대시보드 URL을 알려주세요

- https://ap-northeast-2.console.aws.amazon.com/cloudwatch/home?region=ap-northeast-2#dashboards:name=jeongmin94-dashboard
