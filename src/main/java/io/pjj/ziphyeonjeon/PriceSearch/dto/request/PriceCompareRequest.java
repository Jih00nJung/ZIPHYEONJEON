package io.pjj.ziphyeonjeon.PriceSearch.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PriceCompareRequest {

    // 명세서 맞춤형 필드명 변경
    private List<TargetItem> targets;

    @Data
    public static class TargetItem {
        private String address; // address

        // exclusiveArea -> area_m2
        private BigDecimal area_m2;

        // propertyType -> transaction_type (실제로는 Property Type을 받지만 명세서 필드명 준수)
        private String transaction_type;

        // 가격 편차 계산을 위한 필수 필드
        private Long targetPrice;
    }
}
