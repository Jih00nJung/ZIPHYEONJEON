package io.pjj.ziphyeonjeon.global.API;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

class ApiTest {
    public static void main(String[] args) throws Exception {

        // application-local.properties 파일 로드
        Properties testprop = new Properties();
        try (InputStream input = ApiTest.class.getClassLoader().getResourceAsStream("application-local.properties")) {
            if (input == null) {
                System.err.println("오류: application-local.properties 파일을 찾을 수 없습니다.");
                return;
            }
            testprop.load(input);
        }

        String serviceKey = testprop.getProperty("DISASTER_SERVICE_KEY");

        // API 호출 URL
        String ApiUrl = "https://www.safetydata.go.kr/V2/api/DSSP-IF-10430" +
                "?serviceKey=" + serviceKey;

        URI uri = new URI(ApiUrl);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type", "application/json");

        System.out.println("요청 URL: " + url);

        // 응답
        int responseCode = connection.getResponseCode();
        try (InputStream is = (responseCode >= 200 && responseCode <= 300)
                ? connection.getInputStream()
                : connection.getErrorStream()) {

            if (is == null) {
                System.err.println("응답 스트림을 읽을 수 없습니다.");
                return;
            }

            byte[] rawBytes = is.readAllBytes();
            String result = new String(rawBytes, StandardCharsets.UTF_8);

            // 응답 출력
            System.out.println("응답 코드: " + responseCode);
            System.out.println("---------------------------------------");
            System.out.println("최종 결과:");
            System.out.println(result);
            System.out.println("---------------------------------------");
        } finally {
            connection.disconnect();
        }
    }
}