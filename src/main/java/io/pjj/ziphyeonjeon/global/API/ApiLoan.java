package io.pjj.ziphyeonjeon.global.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiLoan {

    @Value("${PUBLIC_DATA_SERVICE_KEY}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    public ApiLoan(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, String> fetchAllLoanData() {
        Map<String, String> results = new HashMap<>();
        String END_POINT = "https://apis.data.go.kr/1160100/service/GetSmallLoanFinanceInstituteInfoService/getOrdinaryFinanceInfo";

        try {
            String encodedUsage = URLEncoder.encode("주거", StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromUriString(END_POINT)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("likeUsge", encodedUsage)
                    .queryParam("prdExisYn", "Y")
                    .queryParam("numOfRows", 1000)
                    .queryParam("pageNo", 1)
                    .queryParam("resultType", "json")
                    .build(true)
                    .toUri();

            System.out.println("ApiLoan..." + uri);

            String response = restTemplate.getForObject(uri, String.class);
            results.put("allLoans", response);

        } catch (Exception e) {
            System.err.println("ApiLoan 실패: " + e.getMessage());
            results.put("error", "{\"error\": \"ApiLoan 예외 발생: " + e.getMessage() + "\"}");
        }

        return results;
    }
}
