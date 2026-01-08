package io.pjj.ziphyeonjeon.global.API.vworld.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VworldSearchResponse {
    public Result result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        public List<Item> items;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public String title;
        public String address;
        public String road;    // 있을 수도
        public String parcel;  // 있을 수도
        public String pnu;     // ⭐ 중요 (문서/응답에 따라 키명이 다를 수 있음)
        public String x;
        public String y;
    }
}
