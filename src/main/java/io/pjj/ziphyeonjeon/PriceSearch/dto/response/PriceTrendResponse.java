package io.pjj.ziphyeonjeon.PriceSearch.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PriceTrendResponse {

    private String regionName; // "서울특별시 강남구 역삼동"
    private List<TrendItem> trends;

    @Data
    @Builder
    public static class TrendItem {
        private String period; // YYYYMM

        // 단위: 만원/m2 (평단가가 아님, m2당 가격. 필요시 프론트에서 *3.3)
        private Double aptSale;
        private Double aptRent;

        private Double villaSale;
        private Double villaRent;

        private Double officetelSale;
        private Double officetelRent;
    }
}
