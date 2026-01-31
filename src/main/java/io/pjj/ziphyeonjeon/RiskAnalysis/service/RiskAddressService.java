package io.pjj.ziphyeonjeon.RiskAnalysis.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.pjj.ziphyeonjeon.global.config.AddressCodeMap;

@Service
public class RiskAddressService {

    private final AddressCodeMap addressCodeMap;

    public RiskAddressService(AddressCodeMap addressCodeMap) {
        this.addressCodeMap = addressCodeMap;
    }

    // 메인 메서드 - 주소 분리 (시군구, 동, 번, 지)
    public Map<String, String> splitAddressDetails(String address) {
        Map<String, String> addrDetails = new HashMap<>();
        if (address == null || address.isBlank()) return addrDetails;

        String[] parts = address.split("\\s+");

        addrDetails.put("district", splitDistrict(parts));
        addrDetails.put("districtToDong", splitDong(address));

        addrDetails.putAll(splitBunji(parts[parts.length - 1]));

        return addrDetails;
    }

    // 해당 시군구, 법정동 코드 가져오기
    public String[] getAddressCode(String districtToDong) {
        String code = addressCodeMap.getCode(districtToDong);
        if (code == null) return null;

        return new String[]{code.substring(0, 5), code.substring(5)};
    }

    // 시, 군, 구 분리
    private String splitDistrict(String[] parts) {
        if (parts.length >= 2) return parts[0] + " " + parts[1];
        return parts[0];
    }

    // [동읍면리가로] 분리
    // | 를 사용하면 하나씩 대조하지만, 대괄호[] 로 넣으면 이 글자들 중 하나를 찾기 때문에 좋음
    private String splitDong(String address) {
        Pattern pattern = Pattern.compile(".*[동읍면리가로]");
        Matcher matcher = pattern.matcher(address);
        return matcher.find() ? matcher.group().trim() : "";
    }

    // 번, 지 분리
    private Map<String, String> splitBunji(String lastPart) {
        Map<String, String> lotMap = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\d+)(?:-(\\d+))?");
        Matcher matcher = pattern.matcher(lastPart);

        String bun = "0000";
        String ji = "0000";

        if (matcher.find()) {
            bun = toFormatFourDigits(matcher.group(1));
            ji = matcher.group(2) != null ? toFormatFourDigits(matcher.group(2)) : "0000";
        }

        lotMap.put("bun", bun);
        lotMap.put("ji", ji);
        return lotMap;
    }

    // 번, 지 4자리 숫자 문자열로 변환
    private String toFormatFourDigits(String value) {
        return String.format("%04d", Integer.parseInt(value.replaceAll("[^0-9]", "")));
    }
}
