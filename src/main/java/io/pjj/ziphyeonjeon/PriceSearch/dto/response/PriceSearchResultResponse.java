package io.pjj.ziphyeonjeon.PriceSearch.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceSearchResultResponse {
    // 상품 유형 (아파트, 빌라, 오피스텔)
    private String propertyType;
    // 거래 유형 (매매, 전세, 월세)
    private String dealType;
    // 거래 년월 (YYYYMM)
    private Integer contractYm;
    // 거래 일
    private Integer contractDay;
    // 전용면적 (m2)
    private BigDecimal exclusiveArea;
    // 거래 금액 (만원) - 매매/전세금
    private Long dealAmountMan;
    // 월세 (만원) - 월세일 경우
    private Long monthlyRentMan;
    // 층수
    private Integer floor;
    // 건축년도
    private Integer builtYear;

    // 주소 정보 (화면에 보여줄 용도)
    private String sigungu;
    private String dong;
    private String jibun;
    private String roadName;
    private String complexName; // 아파트/오피스텔 이름
}
