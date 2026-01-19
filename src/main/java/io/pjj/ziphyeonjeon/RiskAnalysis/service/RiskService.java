package io.pjj.ziphyeonjeon.RiskAnalysis.service;

import io.pjj.ziphyeonjeon.RiskAnalysis.dto.BuildingTitleDTO;
import io.pjj.ziphyeonjeon.global.API.ApiBuilding;
import io.pjj.ziphyeonjeon.global.config.AddressCodeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskResponseDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.repository.RiskRepository;
import io.pjj.ziphyeonjeon.RiskAnalysis.domain.Risk;

import io.pjj.ziphyeonjeon.RiskAnalysis.domain.DisasterCache;
import io.pjj.ziphyeonjeon.RiskAnalysis.repository.DisasterCacheRepository;
import io.pjj.ziphyeonjeon.global.API.ApiDisaster;

@Service
public class RiskService {

    private final ObjectMapper objectMapper;
    private final RiskRepository riskRepository;

    private final DisasterCacheRepository disasterRepository;
    private final ApiDisaster apiDisaster;
    private final ApiBuilding apiBuilding;
    private final AddressCodeMap addressCodeMap;

    public RiskService(RiskRepository riskRepository, DisasterCacheRepository disasterRepository,
                       ApiDisaster apiDisaster, ApiBuilding apiBuilding, AddressCodeMap addressCodeMap,
                       ObjectMapper objectMapper) {
        this.riskRepository = riskRepository;
        this.disasterRepository = disasterRepository;
        this.apiDisaster = apiDisaster;
        this.apiBuilding = apiBuilding;
        this.addressCodeMap = addressCodeMap;
        this.objectMapper = objectMapper;
    }


    // 재해 API 갱신
    @Transactional
    public void refreshDisasterApiCache() {
        try {
            String rawData = apiDisaster.fetchAllDisasterData();

            JsonNode root = objectMapper.readTree(rawData);
            JsonNode items = root.path("body");

            if (items.isMissingNode() || !items.isArray()) {
                System.out.println("파싱 오류: 데이터가 존재하지 않거나 배열 형식이 아닙니다.");
                return;
            }

            disasterRepository.truncateTable();

            List<DisasterCache> cacheList = new ArrayList<>();
            for (JsonNode item : items) {
                DisasterCache entity = new DisasterCache(
                        item.path("DST_RSK_RGN_RPRS_ADDR").asText("주소 없음"),
                        item.path("LAT").asText("0.0"),
                        item.path("LOT").asText("0.0"),
                        item.path("TYPE").asText("미지정"),
                        item.path("DSGN_RSN").asText("내용 없음"),
                        item.path("REG_DT").asText("")
                );
                cacheList.add(entity);
            }

            disasterRepository.saveAll(cacheList);
            System.out.println("성공: " + cacheList.size() + "건의 재난 API 데이터가 동기화되었습니다.");

        } catch (Exception e) {
            throw new RuntimeException("API 데이터 파싱 및 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // DISASTER_CACHE DB 조회 + 필터링
    @Transactional(readOnly = true)
    public RiskResponseDTO<RiskResponseDTO.DisasterResponse> searchDisasterAddress(String address) {
        List<DisasterCache> entities = disasterRepository.findByAddressContaining(address);

        List<RiskResponseDTO.DisasterResponse> disasterDtoList = entities.stream()
                .map(entity -> new RiskResponseDTO.DisasterResponse(
                        entity.getAddress(),
                        entity.getLat(),
                        entity.getLng(),
                        entity.getType(),
                        entity.getDesignReason(),
                        entity.getRegTime()
                ))
                .collect(Collectors.toList());

        return new RiskResponseDTO<>(
                "SUCCESS",
                entities.isEmpty() ? "검색 결과가 없습니다." : "조회 성공",
                disasterDtoList
        );
    }

    // 주소에서 시군구 추출
    private Map<String, String> parseAddressDetails(String address) {
        Map<String, String> details = new HashMap<>();
        if (address == null || address.isBlank()) return details;

        String[] parts = address.split(" ");
        StringBuilder districtSb = new StringBuilder();

        for (String part : parts) {
            districtSb.append(part).append(" ");
            if (part.endsWith("동") || part.endsWith("읍") || part.endsWith("면") ||
                    part.endsWith("리") || part.endsWith("가")) {
                break;
            }
            if (part.endsWith("구") || part.endsWith("군")) {
            }
        }
        details.put("district", districtSb.toString().trim());

        String numberPart = parts[parts.length - 1].replace("번지", "");
        String bun = "0000";
        String ji = "0000";

        if (numberPart.contains("-")) {
            String[] numSplit = numberPart.split("-");
            bun = formatToFourDigits(numSplit[0].replaceAll("[^0-9]", ""));
            ji = formatToFourDigits(numSplit[1].replaceAll("[^0-9]", ""));
        } else if (numberPart.matches(".*\\d+.*")) {
            bun = formatToFourDigits(numberPart.replaceAll("[^0-9]", ""));
        }

        details.put("bun", bun);
        details.put("ji", ji);
        return details;
    }

    // 4자리 숫자 문자열로 변환
    private String formatToFourDigits(String str) {
        try {
            int num = Integer.parseInt(str);
            return String.format("%04d", num);
        } catch (Exception e) {
            return "0000";
        }
    }

    // 건축물대장 데이터 추출
    private List<BuildingTitleDTO> parseBuildingTitle(String jsonString) {
        if (jsonString == null || jsonString.isBlank()) return Collections.emptyList();

        if (!jsonString.trim().startsWith("{")) {
            System.err.println("API 응답 오류: " + jsonString);
            return Collections.emptyList();
        }

        try {
            JsonNode root = objectMapper.readTree(jsonString);
            JsonNode itemNode = root.path("response").path("body").path("items").path("item");

            if (itemNode.isMissingNode() || itemNode.isNull()) return Collections.emptyList();

            if (itemNode.isArray()) {
                return objectMapper.convertValue(itemNode, new TypeReference<List<BuildingTitleDTO>>() {
                });
            } else {
                return List.of(objectMapper.convertValue(itemNode, BuildingTitleDTO.class));
            }
        } catch (Exception e) {
            System.err.println("JSON 파싱 오류: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // 건축물대장 분석
    public RiskResponseDTO<RiskResponseDTO.BuildingResponse> analyzeBuilding(String address) {
        Map<String, String> addrDetails = parseAddressDetails(address);
        String district = addrDetails.get("district");
        String bun = addrDetails.get("bun");
        String ji = addrDetails.get("ji");

        String code = addressCodeMap.getCode(district);

        if (code == null && district.contains(" ")) {
            String cityAndGu = district.substring(0, district.lastIndexOf(" ")).trim();
            code = addressCodeMap.getCode(cityAndGu);
        }

        if (code == null) {
            return new RiskResponseDTO<>("fail",
                    "법정동 코드를 찾을 수 없습니다: " + district, Collections.emptyList());
        }

        int SIGUNGU_END_INDEX = 5;
        String sigunguCode = code.substring(0, SIGUNGU_END_INDEX);
        String bjdongCode = code.substring(SIGUNGU_END_INDEX);

        Map<String, String> apiRawData = apiBuilding.fetchAllBuildingData(sigunguCode, bjdongCode, bun, ji);
        List<BuildingTitleDTO> titles = parseBuildingTitle(apiRawData.get("title"));

        List<String> reasons = new ArrayList<>();

        int score = calculateBuildingScore(titles, reasons);

        RiskResponseDTO.BuildingResponse buildingResult = new RiskResponseDTO.BuildingResponse(
                address,
                score,
                RiskResponseDTO.BuildingResponse.calculateBuildingLevel(score),
                reasons
        );

        System.out.println("analyzeBuilding -> sigungu: " + sigunguCode + ", bjdong: " + bjdongCode + ", bun: " + bun + ", ji: " + ji);

        return new RiskResponseDTO<>("success", "건축물대장 분석이 완료되었습니다.", List.of(buildingResult));
    }

    // 건축물대장 점수 계산
    private int calculateBuildingScore(List<BuildingTitleDTO> titles, List<String> factors) {
        int score = 100;
        if (titles.isEmpty()) {
            factors.add("조회된 건축물 정보가 없습니다.");
            return 0;
        }

        BuildingTitleDTO title = titles.get(0);

        // 위반건축물 여부 분석
        if ("1".equals(title.indictViolBldYn())) {
            score -= 50;
            factors.add("위반건축물 등재 (-50점)");
        }

        // 주용도 분석
        if (title.mainPurpsCdNm() != null && title.mainPurpsCdNm().contains("근린생활시설")) {
            score -= 30;
            factors.add("근린생활시설 용도 (-30점)");
        }

        // 건물 구조 분석
        if (title.strctCdNm() != null && (title.strctCdNm().contains("벽돌") || title.strctCdNm().contains("조적"))) {
            score -= 5;
            factors.add("취약 구조(조적조 등) (-5점)");
        }

        return Math.max(score, 0);
    }

    // 점수 계산
    private BigDecimal calculateDisasterScore(String grade) {
        if ("안전".equals(grade)) return new BigDecimal("100.00");
        if ("주의".equals(grade)) return new BigDecimal("50.00");
        return new BigDecimal("20.00");
    }

    // 점수 저장
    public Risk scoreSave(Long propertyId, String disasterGrade) {
        BigDecimal score = calculateDisasterScore(disasterGrade);
        String finalGrade = (score.compareTo(new BigDecimal("70")) >= 0) ? "안전" : "위험";

        Risk risk = new Risk(
                propertyId,
                score,
                finalGrade,
                disasterGrade);
        return riskRepository.save(risk);
    }
}