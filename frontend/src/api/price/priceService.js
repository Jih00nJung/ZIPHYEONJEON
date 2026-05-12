/**
 * [MolitAI / 집현전 프로젝트: 통합 부동산 시세 서비스(priceService)]
 * * 기능: 실거래가 검색, 시세 추이 분석, 공시지가 조회, AI 적정가 제안, 단지 디렉토리 관리 등
 * 아키텍처: Axios 기반 apiClient 사용, 모든 백엔드 엔드포인트(P-001 ~ P-010) 완벽 매핑
 * 최적화: 04.30 지침에 따른 스마트 페이징(size: 20/50) 및 GET/POST 메서드 규격 준수
 */

import apiClient from '../apiClient';

export const priceService = {
    
    // ==========================================
    // 1. 기본 검색 및 실거래가 조회 (P-001, P-002)
    // ==========================================

    /**
     * [P-001] 국토교통부 통합 실거래가 검색 (페이징 + 그래프)
     * @param {Object} params - { sigungu, dong, keyword, propertyType, dealType, startMonth, endMonth, page, size }
     * @description 한 번의 호출로 페이징된 매물 리스트(content)와 시세 추이 그래프(trendGraph) 데이터를 동시에 수신합니다[cite: 1, 6].
     */
    searchMolit: async (params) => {
        try {
            // 04.30 지침에 따라 405 에러 방지를 위해 GET 요청으로 수행 [cite: 6]
            const response = await apiClient.get('/api/price/molit', {
                params: {
                    ...params,
                    size: params.size || 20 // 백엔드 권장 페이징 사이즈 적용 [cite: 2]
                }
            });
            return response.data;
        } catch (error) {
            console.error("[Service Error] searchMolit:", error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * 주소 기반 단순 실거래가 검색
     * @param {string} address - 도로명 또는 지번 주소
     * @param {string} dealType - 매매/전세/월세 등 (선택)
     */
    searchByAddress: async (address, dealType = null) => {
        const response = await apiClient.get('/api/price/search', {
            params: { address, dealType }
        });
        return response.data;
    },

    /**
     * 단지명/건물명 기반 실거래가 검색
     * @param {string} complexName - 단지명
     * @param {string} dealType - 거래 유형 (선택)
     */
    searchByComplexName: async (complexName, dealType = null) => {
        const response = await apiClient.get('/api/price/search/complex', {
            params: { complexName, dealType }
        });
        return response.data;
    },

    /**
     * [P-002] 서울시 Open API 기반 실거래가 검색
     * @param {Object} request - { gu_name, deal_type, start_date, end_date }
     */
    searchSeoul: async (request) => {
        const response = await apiClient.get('/api/price/seoul', { params: request });
        return response.data;
    },


    // ==========================================
    // 2. 공시지가 및 지역 정보 (P-003)
    // ==========================================

    /**
     * [P-003] 개별 공시지가 조회
     * @param {Object} request - { uninum_code(PNU), year }
     */
    searchLandPrice: async (request) => {
        const response = await apiClient.get('/api/price/land', { params: request });
        return response.data;
    },

    /**
     * 주소 기반 PNU 코드 조회 (공시지가 조회를 위한 마스터 키)
     * @param {string} address - 주소
     */
    getPnuByAddress: async (address) => {
        const response = await apiClient.get('/api/price/pnu', { params: { address } });
        return response.data; // { pnu: "..." }
    },


    // ==========================================
    // 3. 분석 및 비교 알고리즘 (P-004, P-005, P-008)
    // ==========================================

    /**
     * [P-004] 매물 시세 비교 분석
     * @param {Object} request - { targets: [ { address, area_m2, transaction_type, targetPrice }, ... ] }
     * @description 2개 이상 10개 이하의 매물을 주변 시세와 비교합니다.
     */
    comparePrices: async (request) => {
        const response = await apiClient.post('/api/price/compare', request);
        return response.data;
    },

    /**
     * [P-005] 전세가율 및 깡통전세 위험도 분석
     * @param {Object} request - { address, exclusiveArea, propertyType, jeonse_amount, sigungu, dong }
     * @description 시장 전세가율과 사용자의 전세가율을 비교하여 위험 등급을 산출합니다.
     */
    checkJeonseRisk: async (request) => {
        const response = await apiClient.post('/api/price/risk-check', request);
        return response.data;
    },

    /**
     * [P-008] AI 기반 적정가 제안 및 고/저평가 판단
     * @param {Object} request - { address, area_m2, propertyType, sigungu, dong, market_data: { floor, built_year, current_price } }
     */
    suggestPrice: async (request) => {
        const response = await apiClient.post('/api/price/suggest', request);
        return response.data;
    },


    // ==========================================
    // 4. 시각화 및 데이터 다운로드 (P-006, P-007)
    // ==========================================

    /**
     * [P-006] 지역별 시세 변동 추이 (그래프 전용 데이터)
     * @param {string} sigungu - 시군구 코드/이름
     * @param {string} startMonth - 시작년월 (YYYYMM)
     * @param {string} endMonth - 종료년월 (YYYYMM)
     * @param {string} dong - 법정동 (선택)
     */
    getRegionalTrend: async (sigungu, startMonth, endMonth, dong = null) => {
        const response = await apiClient.get('/api/price/trend', {
            params: { sigungu, dong, startMonth, endMonth }
        });
        return response.data;
    },

    /**
     * [P-007] 실거래 데이터 CSV 다운로드
     * @param {string} sidoCode - 시도 코드
     * @param {string} sigunguCode - 시군구 코드
     */
    downloadTradeData: async (sidoCode, sigunguCode) => {
        try {
            const response = await apiClient.get('/api/price/download', {
                params: { sido_code: sidoCode, sigungu_code: sigunguCode, format: 'csv' },
                responseType: 'blob' // 파일 다운로드를 위해 Blob 타입 지정
            });
            
            // 브라우저 다운로드 트리거
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `trade_data_${sigunguCode}.csv`);
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (error) {
            console.error("[Service Error] downloadTradeData:", error);
            throw error;
        }
    },


    // ==========================================
    // 5. 단지 디렉토리 및 통합 프로필 (P-009, P-010)
    // ==========================================

    /**
     * [P-009] 단지 목록 조회 (Property Directory)
     * @param {Object} filter - { sigungu, dong, propertyType, page, size }
     * @description 건물을 단지/건물 단위로 그룹화하여 리스트를 반환합니다. 
     * 여기서 수신하는 'representativeHouseId'가 이후 모든 상호작용의 마스터 키입니다[cite: 4, 6].
     */
    getPropertyDirectory: async (filter) => {
        const response = await apiClient.post('/api/price/directory', filter);
        return response.data;
    },

    /**
     * [P-010] 매물 배틀 보드용 올인원 프로필 조회
     * @param {number|string} houseId - 매물 고유 ID (representativeHouseId)
     * @description 실거래가, 평당가, 전세가율, AI 예측 결과까지 단 한 번의 호출로 가져오는 핵심 API입니다[cite: 5, 7].
     */
    getPropertyProfile: async (houseId) => {
        const response = await apiClient.get(`/api/price/profile/${houseId}`);
        return response.data;
    },

    /**
     * [P-010-S] 매물 비교 페이지용 경량 프로필 조회
     * @description AI 분석 등 무거운 로직을 제외하고 기본 정보만 빠르게 조회합니다.
     */
    getSimplifiedProfile: async (houseId) => {
        const response = await apiClient.get(`/api/price/profile/${houseId}/simple`);
        return response.data;
    }
};

export default priceService;