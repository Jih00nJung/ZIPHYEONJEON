package io.pjj.ziphyeonjeon.RiskAnalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import io.pjj.ziphyeonjeon.RiskAnalysis.repository.RiskUploadRepository;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.BuildingDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.DisasterDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskResponseDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.entity.RiskUpload;
import io.pjj.ziphyeonjeon.global.API.ApiBuilding;
import io.pjj.ziphyeonjeon.global.API.ApiDisaster;
import io.pjj.ziphyeonjeon.global.config.AddressCodeMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
public class RiskService {

    private final RiskUploadRepository riskUploadRepository;
    private final ObjectMapper objectMapper;

    private final ApiDisaster apiDisaster;
    private final ApiBuilding apiBuilding;
    private final AddressCodeMap addressCodeMap;

    public RiskService(RiskUploadRepository riskUploadRepository, ObjectMapper objectMapper,
                       ApiDisaster apiDisaster, ApiBuilding apiBuilding, AddressCodeMap addressCodeMap
    ) {
        this.riskUploadRepository = riskUploadRepository;
        this.objectMapper = objectMapper;

        this.apiDisaster = apiDisaster;
        this.apiBuilding = apiBuilding;
        this.addressCodeMap = addressCodeMap;
    }

    // 공용 - 주소에서 시군구, 번, 지 추출
    private Map<String, String> parseAddressDetails(String address) {
        Map<String, String> details = new HashMap<>();
        if (address == null || address.isBlank()) return details;

        String[] parts = address.split(" ");

        if (parts.length >= 2) {
            details.put("disasterRegion", parts[0] + " " + parts[1]);
        } else {
            details.put("disasterRegion", parts[0]);
        }

        StringBuilder districtSb = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            districtSb.append(part).append(" ");

            if (part.endsWith("동") || part.endsWith("읍") || part.endsWith("면") ||
                    part.endsWith("리") || part.endsWith("가") || part.endsWith("로")) {
                break;
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

    // 공용 - 번, 지 4자리 숫자 문자열로 변환
    private String formatToFourDigits(String str) {
        try {
            int num = Integer.parseInt(str);
            return String.format("%04d", num);
        } catch (Exception e) {
            return "0000";
        }
    }

    // 공용 - JSON 데이터 추출
    public <T> List<T> parseApiData(String jsonString, Class<T> targetClass, String... paths) {
        if (jsonString == null || jsonString.isBlank()) return Collections.emptyList();

        try {
            JsonNode node = objectMapper.readTree(jsonString);

            for (String path : paths) {
                node = node.path(path);
            }

            if (node.isMissingNode() || node.isNull()) return Collections.emptyList();

            if (node.isArray()) {
                return objectMapper.convertValue(node,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass));
            } else {
                return List.of(objectMapper.convertValue(node, targetClass));
            }
        } catch (Exception e) {
            log.error("JSON 파싱 중 오류 발생: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 공용 - 파일 업로드
    private final String uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "uploads").toString();

    @Transactional
    public String saveFile(String address, MultipartFile file) throws IOException {
        File directory = new File(uploadPath);
        if (!directory.exists()) directory.mkdirs();

        String originalName = file.getOriginalFilename();
        String saveName = UUID.randomUUID().toString() + "_" + originalName;
        Path targetPath = Paths.get(uploadPath, saveName);
        file.transferTo(targetPath);

        RiskUpload upload = new RiskUpload(address, saveName);
        riskUploadRepository.save(upload);

        return saveName;
    }

    // 재해 - 긴급재난문자API에 요청
    public RiskResponseDTO<RiskResponseDTO.DisasterResponse> sendDisasterApi(String address) {
        Map<String, String> addrDetails = parseAddressDetails(address);
        String rgnNm = addrDetails.get("disasterRegion");

        String rawData = apiDisaster.fetchAllDisasterData(rgnNm);
        List<DisasterDTO> disasterList = parseApiData(rawData, DisasterDTO.class, "body");

        List<RiskResponseDTO.DisasterResponse> disasterResults = disasterList.stream()
                .filter(data -> data.EMRG_STEP_NM() != null && data.EMRG_STEP_NM().contains("재난"))
                .map(data -> new RiskResponseDTO.DisasterResponse(
                        data.RCPTN_RGN_NM(),
                        data.EMRG_STEP_NM(),
                        data.DST_SE_NM(),
                        data.REG_YMD()
                ))
                .toList();

        return new RiskResponseDTO<>("success", address + "의 재해 분석 완료", disasterResults);
    }


    // 건축물대장 - 건축물대장API에 요청
    public RiskResponseDTO<RiskResponseDTO.BuildingResponse> sendBuildingApi(String address) {
        Map<String, String> addrDetails = parseAddressDetails(address);
        String district = addrDetails.get("district");
        String bun = addrDetails.get("bun");
        String ji = addrDetails.get("ji");

        String code = addressCodeMap.getCode(district);

        // 시군구, 법정동코드 분리
        if (code == null && district.contains(" ")) {
            String siGu = district.substring(0, district.lastIndexOf(" ")).trim();
            code = addressCodeMap.getCode(siGu);
        }
        if (code == null) {
            return new RiskResponseDTO<>("fail",
                    "시군구, 법정동 코드를 찾을 수 없습니다: " + district, Collections.emptyList());
        }

        int SIGUNGU_END_INDEX = 5;
        String sigunguCode = code.substring(0, SIGUNGU_END_INDEX);
        String bjdongCode = code.substring(SIGUNGU_END_INDEX);

        // 응답 데이터 출력
        Map<String, String> apiRawData = apiBuilding.fetchAllBuildingData(sigunguCode, bjdongCode, bun, ji);
        List<BuildingDTO> titles = parseApiData(apiRawData.get("title"), BuildingDTO.class, "response", "body", "items", "item");

        List<String> reasons = new ArrayList<>();

        int score = calculateBuildingScore(titles, reasons);

        RiskResponseDTO.BuildingResponse buildingResult = new RiskResponseDTO.BuildingResponse(
                address,
                score,
                RiskResponseDTO.BuildingResponse.calculateBuildingLevel(score),
                reasons
        );

        return new RiskResponseDTO<>("success", address + "의 건축물대장 분석 완료", List.of(buildingResult));
    }

    // 건축물대장 - 건축물대장 점수 계산
    private int calculateBuildingScore(List<BuildingDTO> titles, List<String> factors) {
        int score = 100;
        if (titles.isEmpty()) {
            factors.add("조회된 건축물 정보가 없습니다.");
            return 0;
        }

        BuildingDTO title = titles.get(0);

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


}