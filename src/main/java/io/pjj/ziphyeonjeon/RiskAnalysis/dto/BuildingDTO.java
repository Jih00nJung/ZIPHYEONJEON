package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BuildingDTO(
        String bldNm,               // 건물명
        String platPlc,             // 대지위치
        String rnum,                // 순번
        String mainPurpsCdNm,       // 주용도코드명
        String etcPurps,            // 기타용도
        int grndFlrCnt,             // 지상층수
        int ugrndFlrCnt,            // 지하층수
        String indictViolBldYn,     // 위반건축물 여부 (0: 정상, 1: 위반)
        String strctCdNm,           // 구조명
        String rtitnMainPurpsCdNm   // 지붕구조명
) {
    public record BuildingResponse(
            String address,
            int finalScore,
            String riskLevel,           // 등급
            List<String> riskFactors    // 감점 사유
    ) {
        public static String calculateBuildingLevel(int score) {
            if (score >= 95) return "안전";
            if (score >= 65) return "주의";
            if (score >= 5) return "위험";
            return "조회된 건축물 정보가 없습니다.";
        }
    }
}