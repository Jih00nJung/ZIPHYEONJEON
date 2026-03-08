package io.pjj.ziphyeonjeon.global.API.vworld.dto.landprice;

public class VworldOfficialLandPriceResponse {
    // TODO: 실제 응답 스키마에 맞춰 필드 추가
    private String raw;

    public VworldOfficialLandPriceResponse() {}
    public VworldOfficialLandPriceResponse(String raw) { this.raw = raw; }

    public String getRaw() { return raw; }
    public void setRaw(String raw) { this.raw = raw; }
}
