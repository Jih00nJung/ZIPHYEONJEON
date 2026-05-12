package io.pjj.ziphyeonjeon.industry.dto;

public record IndustryResponse(
        String adstrdCd,
        String adstrdNm,
        String svcIndutyCd,
        String svcIndutyNm,
        Integer shopCount,
        Double opbizRt,
        Integer opbizShopCount,
        Double clsbizRt,
        Integer clsbizShopCount,
        Integer frcShopCount
) {}
