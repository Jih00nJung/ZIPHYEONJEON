package io.pjj.ziphyeonjeon.global.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiDisaster {

    @Value("${DISASTER_SERVICE_KEY}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    public ApiDisaster(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchAllDisasterData(String rgnNm) {

        String BASE_URL = "https://www.safetydata.go.kr/V2/api/DSSP-IF-00247";

        String url = BASE_URL + "?serviceKey=" + serviceKey
                + "&rgnNm=" + rgnNm
                + "&numOfRows=1000&pageNo=1&returnType=json";

        try {
            System.out.println("ApiDisaster...");
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "{\"error\": \"ApiDisaster 실패: " + e.getMessage() + "\"}";
        }
    }
}
