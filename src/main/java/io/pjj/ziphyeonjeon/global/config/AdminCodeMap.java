package io.pjj.ziphyeonjeon.global.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminCodeMap {
    private final Map<String, String> codeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("common/KIKcd_H.20250113.txt");

        if (is == null) {
            System.err.println("파일을 찾을 수 없습니다: common/KIKcd_H.20250113.txt");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // 헤더 스킵

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    // 말소일자(parts[5])가 있으면 폐지된 코드이므로 제외
                    if (parts.length >= 6 && !parts[5].trim().isEmpty()) {
                        continue;
                    }

                    String code = parts[0].trim();
                    
                    // 주소 구성: 시도명 + 시군구명 + 읍면동명
                    String sido = parts[1].trim();
                    String sigungu = parts[2].trim();
                    String dong = parts[3].trim();
                    
                    StringBuilder sb = new StringBuilder(sido);
                    if (!sigungu.isEmpty()) sb.append(" ").append(sigungu);
                    if (!dong.isEmpty()) sb.append(" ").append(dong);
                    
                    String fullAddress = sb.toString();

                    // 10자리 코드의 마지막 00을 잘라서 8자리 행정동 코드로 변환
                    if (code.length() == 10) {
                        code = code.substring(0, 8);
                    }
                    
                    codeMap.put(fullAddress, code);

                    // 시군구 + 읍면동 조합으로도 찾을 수 있도록 추가 (예: 강남구 수서동)
                    if (!sigungu.isEmpty() && !dong.isEmpty()) {
                        String sigunguDong = sigungu + " " + dong;
                        if (!codeMap.containsKey(sigunguDong) || code.startsWith("11")) {
                            codeMap.put(sigunguDong, code);
                        }
                    }

                    // 읍면동명만으로도 찾을 수 있도록 추가 (이미 존재할 경우 서울(11) 코드를 우선시함)
                    if (!dong.isEmpty()) {
                        if (!codeMap.containsKey(dong) || code.startsWith("11")) {
                            codeMap.put(dong, code);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCode(String address) {
        return codeMap.get(address);
    }
}
