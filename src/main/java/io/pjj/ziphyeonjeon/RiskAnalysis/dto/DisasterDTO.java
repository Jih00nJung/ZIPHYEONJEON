package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DisasterDTO(
        String RCPTN_RGN_NM,    // 수신지역명
        String EMRG_STEP_NM,    // 긴급단계명
        String DST_SE_NM,       // 재해구분명
        String REG_YMD          // 등록일자
) {
    public record DisasterResponse(
            String address,
            Integer score,
            List<DisasterDTO> disasterData
    ) {
        public static String calculateDisasterLevel(Integer score) {
            if (score >= 90) return "안전";
            if (score >= 70) return "주의";
            return "위험";
        }
    }

}

