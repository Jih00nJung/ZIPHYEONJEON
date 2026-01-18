package io.pjj.ziphyeonjeon.global.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

// 할 일
// 1. API 데이터를 캐싱 처리하기
// 2. 캐싱 처리한 데이터에 필터 걸어서 조회 메서드 만들기

// 세부 요청변수를 허용하지 않기 때문에
// 하루에 한 번 로컬 DB에 전체 데이터를 저장해서 캐싱하는 방법으로 사용해야 할 듯?
// 추가적으로 스프링부트에는 @Cashable 어노테이션으로 캐싱할 수 있다고 한다. 추후 찾아보자

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
