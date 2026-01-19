package io.pjj.ziphyeonjeon.global.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiDisaster {

    @Value("${DISASTER_SERVICE_KEY}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchAllDisasterData() {

        String BASE_URL = "https://www.safetydata.go.kr/V2/api/DSSP-IF-10430";
        String url = BASE_URL + "?serviceKey=" + serviceKey + "&numOfRows=1000&pageNo=1";

        try {
            System.out.println("API 호출중");
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "{\"error\": \"API 호출 실패: " + e.getMessage() + "\"}";
        }
    }
}
