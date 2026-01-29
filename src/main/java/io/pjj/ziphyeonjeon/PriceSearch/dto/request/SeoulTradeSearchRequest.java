package io.pjj.ziphyeonjeon.PriceSearch.dto.request;

import lombok.Data;

@Data
public class SeoulTradeSearchRequest {
    // Spec: gu_name, deal_type, start_date, end_date
    private String gu_name;
    private String deal_type;
    private String start_date; // YYYYMMDD
    private String end_date; // YYYYMMDD
}
