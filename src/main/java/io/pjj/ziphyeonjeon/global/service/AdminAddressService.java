package io.pjj.ziphyeonjeon.global.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.pjj.ziphyeonjeon.global.config.AdminCodeMap;

@Service
public class AdminAddressService {

    private final AdminCodeMap adminCodeMap;

    public AdminAddressService(AdminCodeMap adminCodeMap) {
        this.adminCodeMap = adminCodeMap;
    }

    public String getAdstrdCd(String address) {
        if (address == null || address.isBlank()) {
            return null;
        }

        String normalizedAddress = normalizeSido(address.trim());
        
        String districtToDong = extractDistrictToDong(normalizedAddress);
        String code = null;

        if (!districtToDong.isEmpty()) {
            code = adminCodeMap.getCode(districtToDong);
        }

        // 전체 매칭 실패 시 읍면동명만으로 다시 시도
        if (code == null) {
            String onlyDong = extractOnlyDong(normalizedAddress);
            if (!onlyDong.isEmpty()) {
                code = adminCodeMap.getCode(onlyDong);
            }
        }

        // 여전히 실패 시 원본 문자열로 시도
        if (code == null) {
            code = adminCodeMap.getCode(normalizedAddress);
        }

        return code;
    }

    /**
     * 주소에서 읍면동명만 추출합니다. (마지막 단어)
     */
    private String extractOnlyDong(String address) {
        Pattern pattern = Pattern.compile("([^\\s]+[동읍면리])$");
        Matcher matcher = pattern.matcher(address.trim());
        return matcher.find() ? matcher.group(1) : "";
    }

    /**
     * 시도명을 표준 명칭으로 정규화합니다. (예: 서울 -> 서울특별시, 경기 -> 경기도)
     */
    private String normalizeSido(String address) {
        if (address.startsWith("서울 ")) return address.replaceFirst("서울 ", "서울특별시 ");
        if (address.startsWith("서울시 ")) return address.replaceFirst("서울시 ", "서울특별시 ");
        if (address.startsWith("경기 ")) return address.replaceFirst("경기 ", "경기도 ");
        if (address.startsWith("인천 ")) return address.replaceFirst("인천 ", "인천광역시 ");
        if (address.startsWith("인천시 ")) return address.replaceFirst("인천시 ", "인천광역시 ");
        if (address.startsWith("부산 ")) return address.replaceFirst("부산 ", "부산광역시 ");
        if (address.startsWith("부산시 ")) return address.replaceFirst("부산시 ", "부산광역시 ");
        if (address.startsWith("대구 ")) return address.replaceFirst("대구 ", "대구광역시 ");
        if (address.startsWith("대구시 ")) return address.replaceFirst("대구시 ", "대구광역시 ");
        if (address.startsWith("광주 ")) return address.replaceFirst("광주 ", "광주광역시 ");
        if (address.startsWith("광주시 ")) return address.replaceFirst("광주시 ", "광주광역시 ");
        if (address.startsWith("대전 ")) return address.replaceFirst("대전 ", "대전광역시 ");
        if (address.startsWith("대전시 ")) return address.replaceFirst("대전시 ", "대전광역시 ");
        if (address.startsWith("울산 ")) return address.replaceFirst("울산 ", "울산광역시 ");
        if (address.startsWith("울산시 ")) return address.replaceFirst("울산시 ", "울산광역시 ");
        if (address.startsWith("세종 ")) return address.replaceFirst("세종 ", "세종특별자치시 ");
        if (address.startsWith("세종시 ")) return address.replaceFirst("세종시 ", "세종특별자치시 ");
        if (address.startsWith("충남 ")) return address.replaceFirst("충남 ", "충청남도 ");
        if (address.startsWith("충북 ")) return address.replaceFirst("충북 ", "충청북도 ");
        if (address.startsWith("경남 ")) return address.replaceFirst("경남 ", "경상남도 ");
        if (address.startsWith("경북 ")) return address.replaceFirst("경북 ", "경상북도 ");
        if (address.startsWith("전남 ")) return address.replaceFirst("전남 ", "전라남도 ");
        if (address.startsWith("전북 ")) return address.replaceFirst("전북 ", "전라북도 ");
        if (address.startsWith("제주 ")) return address.replaceFirst("제주 ", "제주특별자치도 ");
        if (address.startsWith("제주시 ")) return address.replaceFirst("제주시 ", "제주특별자치도 "); // 주의: 제주특별자치도 제주시... 형태일 수 있음
        return address;
    }

    /**
     * [동읍면리] 까지의 주소를 추출합니다.
     * 예: "서울특별시 강남구 역삼1동 123-45" -> "서울특별시 강남구 역삼1동"
     */
    private String extractDistrictToDong(String address) {
        // 행정동은 보통 '동', '읍', '면'으로 끝납니다. '리'는 행정동 단위에서는 드뭅니다.
        Pattern pattern = Pattern.compile(".*?[\\s|^]([^\\s]+[동읍면리])");
        Matcher matcher = pattern.matcher(address);
        return matcher.find() ? matcher.group().trim() : "";
    }
}
