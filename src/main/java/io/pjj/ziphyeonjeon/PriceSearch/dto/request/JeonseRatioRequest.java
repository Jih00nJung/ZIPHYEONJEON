package io.pjj.ziphyeonjeon.PriceSearch.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class JeonseRatioRequest {
    // address
    private String address;

    // 유형 (아파트, 빌라 등) - 명세서엔 없지만 필수 식별자이므로 유지
    private String propertyType;

    // 전용면적 - 명세서(P-004 참조) 스타일 통일 고려, 여기서는 기존 유지하되 명세서 맥락 반영
    private BigDecimal exclusiveArea;

    // targetDeposit -> jeonse_amount
    private Long jeonse_amount;

    // market_price (선택 입력: 사용자 지정 매매가)
    private Long market_price;
}
