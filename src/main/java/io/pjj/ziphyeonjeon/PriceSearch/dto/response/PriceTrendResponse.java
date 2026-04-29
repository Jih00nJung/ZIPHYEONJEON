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

        // 단위: 만원/m2 (매매, 전세) - 현재는 3.3m2(평) 단위
        private Double aptSale;
        private Double aptJeonse;
        private Double aptWolseDeposit; // 단위: 만원 (월세 보증금)
        private Double aptWolseRent;    // 단위: 만원 (월세액)

        private Double villaSale;
        private Double villaJeonse;
        private Double villaWolseDeposit; // 단위: 만원 (월세 보증금)
        private Double villaWolseRent;    // 단위: 만원 (월세액)

        private Double officetelSale;
        private Double officetelJeonse;
        private Double officetelWolseDeposit; // 단위: 만원 (월세 보증금)
        private Double officetelWolseRent;    // 단위: 만원 (월세액)
    }
}
