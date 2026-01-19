package io.pjj.ziphyeonjeon.global.API;

import io.pjj.ziphyeonjeon.global.config.AddressCodeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiBuilding {

    @Value("${BUILDING_SERVICE_KEY}")
    private String serviceKey;

    private final RestTemplate restTemplate;
    private final AddressCodeMap addressCodeMap;

    public ApiBuilding(RestTemplate restTemplate, AddressCodeMap addressCodeMap) {
        this.restTemplate = restTemplate;
        this.addressCodeMap = addressCodeMap;
    }

    public Map<String, String> fetchAllBuildingData(String sigunguCd, String bjdongCd, String bun, String ji) {

        Map<String, String> results = new HashMap<>();

        String BASE_URL = "https://apis.data.go.kr/1613000/BldRgstHubService";

        Map<String, String> operations = new HashMap<>();
        operations.put("title", "/getBrTitleInfo");     // 표제부 조회
        operations.put("jijigu", "/getBrJijiguInfo");   // 지역지구구역 조회
        operations.put("expos", "/getBrExposInfo");     // 전유부 조회

        operations.forEach((key, operation) -> {
            try {
                String urlString = BASE_URL + operation
                        + "?serviceKey=" + serviceKey
                        + "&sigunguCd=" + sigunguCd
                        + "&bjdongCd=" + bjdongCd
                        + "&bun" + bun
                        + "&ji" + ji
                        + "&platGbCd=0&numOfRows=10&pageNo=1&_type=json";

                URI uri = new URI(urlString);

                System.out.println(key + " BUILDING API 호출 중... (URL 확인: " + operation + ")");

                String response = restTemplate.getForObject(uri, String.class);
                results.put(key, response);

            } catch (Exception e) {
                System.err.println(key + " API 호출 실패: " + e.getMessage());
                results.put(key, "{\"error\": \"API 호출 중 예외 발생: " + e.getMessage() + "\"}");
            }
        });

        return results;
    }
}
