# MolitAI

## 📌 개요
MolitAI는 집현전 부동산 서비스에서 사용하는 주택 가격 예측 AI 모듈입니다.  
실거래가 + KB 지수 + 금리 데이터를 결합하여 지역 단위 월별 가격 예측 모델을 학습했습니다.

---

## 📁 폴더 구조

MolitAI/
├── models/
│   ├── sale/
│   │   ├── sale_h1m.pkl
│   │   ├── sale_h3m.pkl
│   │   └── sale_h6m.pkl
│   └── rent/
│       ├── rent_h1m.pkl
│       └── rent_h3m.pkl
│
├── config/
│   ├── feature_columns.json
│   ├── categorical_columns.json
│   └── model_meta.json
│
├── preprocess/
│   └── preprocessing.py

---

## 🧠 모델 종류

### 매매
- sale_h1m.pkl → 1개월 후
- sale_h3m.pkl → 3개월 후
- sale_h6m.pkl → 6개월 후

### 전월세
- rent_h1m.pkl → 1개월 후
- rent_h3m.pkl → 3개월 후

---

## 📊 사용 데이터

### 실거래가
- 기간: 2024 ~ 2026.03
- 유형:
  - 아파트
  - 오피스텔
  - 빌라
- 거래:
  - 매매 / 전월세

### 외부 데이터
- KB 매매가격지수
- KB 전세가율
- 한국 10년물 금리 (KR10YT)

---

## ⚙️ 학습 방식

### 1. 월 단위 집계
시도 + 주택유형 + 거래유형 + 연월 기준으로 집계

### 2. 주요 feature
- 평균가격
- 면적당 가격
- 거래량
- 건물 나이
- KB 지수
- 전세가율
- 금리

### 3. 시계열 feature
- lag (1, 3, 6개월)
- rolling mean
- 변화율

### 4. 타깃
- 미래 가격 (1, 3, 6개월)
- log 변환 사용

---

## 🤖 모델

- LightGBM Regressor 사용

### 특징
- 트리 기반 모델
- 비선형 패턴 학습
- 범주형 자동 처리

※ 드롭아웃 / 시그모이드 사용 안함  
→ 딥러닝이 아니라 트리 모델이기 때문

---

## 📈 성능 (Test 기준)

### 매매
- 1개월: R² ≈ 0.90
- 3개월: R² ≈ 0.92
- 6개월: R² ≈ 0.91

### 전월세
- 1개월: R² ≈ 0.96
- 3개월: R² ≈ 0.93

---

## 🚨 사용 시 필수 규칙

### 1. 모델만 쓰면 안됨
반드시 같이 사용:
- feature_columns.json
- categorical_columns.json
- preprocessing.py

### 2. feature 순서 유지
순서 틀리면 예측값 깨짐

### 3. 전처리 동일하게 유지
학습과 다르면 정확도 붕괴

---

## 🔄 예측 흐름

입력 데이터
→ 전처리
→ feature 생성
→ feature 정렬
→ 모델 예측

---

## 💻 Python 예시

```python
import joblib
import json
import pandas as pd
import numpy as np

# 설정 로드
with open("config/feature_columns.json", "r", encoding="utf-8") as f:
    feature_cols = json.load(f)

with open("config/categorical_columns.json", "r", encoding="utf-8") as f:
    cat_cols = json.load(f)

# 모델 로드
model = joblib.load("models/sale/sale_h1m.pkl")

# 입력 데이터
X = pd.read_csv("input.csv")

# feature 순서 맞추기
X = X[feature_cols]

# 범주형 처리
for c in cat_cols:
    if c in X.columns:
        X[c] = X[c].astype("category")

# 예측
pred = model.predict(X)

# 로그 복원
pred = np.expm1(pred)

print(pred)


## 🏗 Spring Boot 사용 방식

추천 구조:

Spring Boot  
→ Python API (FastAPI)  
→ 모델 예측  
→ 결과 반환

***

## ✅ 핵심 정리

이 모델은 단순 파일이 아니라

전처리 + feature + 모델

이 세가지가 같이 동작해야 정상 작동한다.