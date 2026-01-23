package io.pjj.ziphyeonjeon.global.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ApiNaverOcr {

    @Value("${NAVER_APIGW_INVOKE_URL}")
    private String serviceUrl;

    @Value("${NAVER_OCR_SERVICE_KEY}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    public ApiNaverOcr(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callNaverOcr(String message, MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("X-OCR-SECRET", serviceKey);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("message", message);
            body.add("file", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            System.out.println("네이버 OCR API 호출 중...");
            ResponseEntity<String> response = restTemplate.exchange(
                    serviceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            System.err.println("OCR API 호출 실패: " + e.getMessage());
            return "{\"error\": \"OCR API 호출 실패: " + e.getMessage() + "\"}";
        }
    }
}
