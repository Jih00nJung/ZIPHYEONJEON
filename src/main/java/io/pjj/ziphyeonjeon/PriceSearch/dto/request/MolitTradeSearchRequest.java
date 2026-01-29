package io.pjj.ziphyeonjeon.PriceSearch.dto.request;

import lombok.Data;

@Data
public class MolitTradeSearchRequest {
    // Spec: sigungu_code, buliding_type, deal_type, deal_year_month
    private String sigungu_code;
    private String building_type; // 아파트/빌라 등
    private String deal_type; // 매매/전세
    private String deal_year_month; // YYYYMM
}
