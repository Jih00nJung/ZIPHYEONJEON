package io.pjj.ziphyeonjeon.PriceSearch.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JeonseRatioResponse {

    // 분석 기준 정보
    private String address;
    private Long avgSalePrice; // 동네 평균 매매가
    private Long avgJeonsePrice; // 동네 평균 전세가

    // 분석 결과 (시장 기준)
    private Double marketJeonseRatio; // 시장 전세가율 (평균전세/평균매매)

    // 분석 결과 (사용자 매물 기준)
    private Double myJeonseRatio; // 내 매물 전세가율 (내보증금/평균매매)

    // 위험도 (SAFE, CAUTION, DANGER)
    private String riskLevel;

    // 상세 코멘트
    private String riskMessage;
}
