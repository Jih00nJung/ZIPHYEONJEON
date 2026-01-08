package io.pjj.ziphyeonjeon.global.API.vworld.dto.geocode;
//기능: VWorld 전체 응답 래핑(상태코드/메시지/결과)
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VworldGeocodeResponse {
    public Response response;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        public String status;
        public Result result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        public Point point;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Point {
        public String x;
        public String y;
    }
}
