package io.pjj.ziphyeonjeon.PriceSearch.dto.request;

import lombok.Data;

@Data
public class OfficialLandPriceRequest {
    // Spec: uninum_code (필수), year
    private String uninum_code; // PNU
    private String year;
}
