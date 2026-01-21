package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RiskResponseDTO<T>(
        String status,
        String message,
        List<T> data

) {

    public record DisasterResponse(
            String RCPTN_RGN_NM,    // 수신지역명
            String EMRG_STEP_NM,    // 긴급단계명
            String DST_SE_NM,       // 재해구분명
            String REG_YMD          // 등록일자
    ) {
    }

    public record BuildingResponse(
            String address,
            int finalScore,
            String riskLevel,           // 등급
            List<String> riskFactors    // 감점 사유
    ) {
        public static String calculateBuildingLevel(int score) {
            if (score >= 90) return "안전";
            if (score >= 70) return "주의";
            return "위험";
        }
    }

}