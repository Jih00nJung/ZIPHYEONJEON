package io.pjj.ziphyeonjeon.PriceSearch.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PriceSuggestionResponse {
    // Spec: suggested_price, calculation_basis
    private Long suggested_price; // AI 제안 적정가 (만원 Unit)
    private String grade; // 판단 등급 (저평가/적정/고평가)

    private CalculationBasis calculation_basis;

    @Data
    @Builder
    public static class CalculationBasis {
        private String avg_market_price; // "주변 30평대 평균 시세: 5억 2천"
        private List<String> adjustments; // ["연식 -5년 감가상각 (-2%)", "로열층 프리미엄 (+5%)"]
        private String algorithm_version; // "v1.0 (Comparable Sales Adjustment)"
    }
}
