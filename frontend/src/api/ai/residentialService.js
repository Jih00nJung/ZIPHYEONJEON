/**
 * [Residential AI Prediction Service - Final Integrated]
 * 백엔드 API 규격 완벽 준수
 */
import apiClient from '../apiClient'; 

export const residentialService = {
    /**
     * [P-008] 실시간 집값 예측 요청
     */
    predict: async (payload) => {
        try {
            const response = await apiClient.post('/api/v1/ai/predict', payload);
            return response.data;
        } catch (error) {
            console.error("AI 예측 API 에러:", error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * [P-011] 마이페이지 AI 분석 기록 조회
     */
    getHistory: async () => {
        try {
            const response = await apiClient.get('/api/v1/ai/predict/me');
            return response.data;
        } catch (error) {
            console.error("분석 기록 조회 에러:", error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * 특정 매물의 종합 프로필 조회 (느림)
     */
    getPropertyProfile: async (houseId) => {
        try {
            const response = await apiClient.get(`/api/price/profile/${houseId}`);
            return response.data;
        } catch (error) {
            console.error("종합 프로필 조회 에러:", error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * [P-009] 단지/도로명 검색 API
     */
    searchDirectory: async (filter) => {
        try {
            const response = await apiClient.post('/api/price/directory', filter);
            return response.data;
        } catch (error) {
            console.error("단지 검색 API 에러:", error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * [P-010-S] 매물 입력용 경량 프로필 조회 (빠름)
     */
    getSimplifiedProfile: async (houseId) => {
        try {
            const response = await apiClient.get(`/api/price/profile/${houseId}/simple`);
            return response.data;
        } catch (error) {
            console.error("경량 프로필 조회 에러:", error.response?.data || error.message);
            throw error;
        }
    }
};
