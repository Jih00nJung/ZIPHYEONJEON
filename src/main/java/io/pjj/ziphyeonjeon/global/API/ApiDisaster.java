package io.pjj.ziphyeonjeon.global.API;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

// 할 일
// 1. API 데이터를 캐싱 처리하기
// 2. 캐싱 처리한 데이터에 필터 걸어서 조회 메서드 만들기

// 세부 요청변수를 허용하지 않기 때문에
// 하루에 한 번 로컬 DB에 전체 데이터를 저장해서 캐싱하는 방법으로 사용해야 할 듯?
// 추가적으로 스프링부트에는 @Cashable 어노테이션으로 캐싱할 수 있다고 한다. 추후 찾아보자


public class ApiDisaster {
    public static void main(String[] args) throws Exception {

        // application-local.properties 파일 로드
        Properties prop = new Properties();
        try (InputStream input = ApiTest.class.getClassLoader().getResourceAsStream("application-local.properties")) {
            if (input == null) {
                System.err.println("오류: application-local.properties 파일을 찾을 수 없습니다.");
                return;
            }
            prop.load(input);
        }

        String serviceKey = prop.getProperty("DISASTER_SERVICE_KEY");

        // API 호출 URL
        String urlDisaster = "https://www.safetydata.go.kr/V2/api/DSSP-IF-10430" +
                "?serviceKey=" + serviceKey +
                "&numOfRows=10" +
                "&pageNo=1";

        URI uri = new URI(urlDisaster);
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
                System.err.println("응답을 읽을 수 없습니다.");
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
