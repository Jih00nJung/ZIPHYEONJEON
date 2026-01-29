package io.pjj.ziphyeonjeon.PriceSearch.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PriceCompareResponse {

    // 원본 정보
    private String address;
    private String propertyType;
    private BigDecimal exclusiveArea;
    private Long targetPrice;

    // 분석 정보
    private Long averageMarketPrice; // 주변 시세 (평균)
    private Long priceDiff; // 시세 차익 (Target - Average) -> 양수면 비쌈, 음수면 쌈
    private Double diffPercent; // 퍼센트 차이
    private String analysisMessage; // "시세보다 5% 저렴합니다" 등 간단 코멘트
}
