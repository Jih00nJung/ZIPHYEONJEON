package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RiskResponseDTO(
        String status,
        String message,
        List<DisasterResponse> data
) {

    public record DisasterResponse(
            @JsonProperty("DST_RSK_RGN_RPRS_ADDR") String address,  // "전라남도 영광군 대마면 성산리 일원"
            @JsonProperty("LAT") String lat,                        // 35.319242
            @JsonProperty("LOT") String lng,                        // 126.618403
            @JsonProperty("TYPE") String type,                      // "침수위험"
            @JsonProperty("DSGN_RSN") String designReason,          // "지형적인 여건 및 풍수해 영향에 의해 재해가 발생하였거나 우려가 있는 지역"
            @JsonProperty("REG_DT") String regTime                  // "2021/06/15 16:01:22"

    ) {

    }

}