package io.pjj.ziphyeonjeon.industry.service;

import io.pjj.ziphyeonjeon.industry.dto.IndustryResponse;

import java.util.List;

public interface IndustryService {
    void syncLocalCsv();

    List<IndustryResponse> getIndustryByAdstrdCd(String adstrdCd);

    List<IndustryResponse> getIndustryBySvcIndutyNm(String svcIndutyNm);
}
