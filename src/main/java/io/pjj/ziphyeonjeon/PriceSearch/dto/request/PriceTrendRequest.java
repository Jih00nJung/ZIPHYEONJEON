package io.pjj.ziphyeonjeon.PriceSearch.dto.request;

import lombok.Data;

@Data
public class PriceTrendRequest {
    // 주소 (예: 서울 강남구 역삼동) - '동' 단위까지 필수
    private String address;
}
