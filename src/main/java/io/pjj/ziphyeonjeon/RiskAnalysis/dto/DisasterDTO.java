package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DisasterDTO(
        @JsonProperty("RCPTN_RGN_NM") String RCPTN_RGN_NM,    // 수신지역명
        @JsonProperty("EMRG_STEP_NM") String EMRG_STEP_NM,    // 긴급단계명
        @JsonProperty("DST_SE_NM") String DST_SE_NM,          // 재해구분명
        @JsonProperty("REG_YMD") String REG_YMD               // 등록일자
) {
}

