package io.pjj.ziphyeonjeon.PriceSearch.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PriceSuggestionRequest {
    // Spec: address, area_m2, market_data
    private String address;
    private BigDecimal area_m2;

    // 사용자가 입력하는 매물 상세 정보 (보정 계수 산출용)
    private MarketData market_data;

    @Data
    public static class MarketData {
        private Integer built_year; // 건축년도 (연식 보정용)
        private Integer floor; // 층수 (층별 보정용)
        private Long current_price; // 현재 호가 (비교용, 선택)
    }
}
