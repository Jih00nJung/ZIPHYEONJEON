package io.pjj.ziphyeonjeon.RiskAnalysis.service;

import io.pjj.ziphyeonjeon.RiskAnalysis.dto.OcrDTO;
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
import io.pjj.ziphyeonjeon.global.API.ApiNaverOcr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
public class RiskService {

    private final RiskUploadRepository riskUploadRepository;
    private final ObjectMapper objectMapper;

    private final ApiDisaster apiDisaster;
    private final ApiBuilding apiBuilding;
    private final AddressCodeMap addressCodeMap;
    private final ApiNaverOcr apiNaverOcr;

    public RiskService(RiskUploadRepository riskUploadRepository, ObjectMapper objectMapper,
                       ApiDisaster apiDisaster, ApiBuilding apiBuilding, AddressCodeMap addressCodeMap,
                       ApiNaverOcr apiNaverOcr
    ) {
        this.riskUploadRepository = riskUploadRepository;
        this.objectMapper = objectMapper;

        this.apiDisaster = apiDisaster;
        this.apiBuilding = apiBuilding;
        this.addressCodeMap = addressCodeMap;
        this.apiNaverOcr = apiNaverOcr;
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
    public String saveFile(String address, String requestId, MultipartFile file) throws IOException {
        File directory = new File(uploadPath);
        if (!directory.exists()) directory.mkdirs();

        String originalName = file.getOriginalFilename();
        String saveName = requestId + "_" + originalName;
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

    // 등기부등본 요청 -> 추출 -> 점수 계산
    @Transactional
    public RiskResponseDTO<RiskResponseDTO.RecordOfTitleResponse> analyzeRecordOfTitleRisk(String message, MultipartFile file) {
        OcrDTO ocrData = extractText(message, file);
        RiskResponseDTO.RecordOfTitleResponse RecordOfTitleResult = calculateRecordOfTitleScore(ocrData);

        return new RiskResponseDTO<>("success", "등기부등본 분석 완료", List.of(RecordOfTitleResult));
    }

    // 등기부등본 - 네이버 OCR 요청
    @Transactional
    public String sendToNaverOcr(String message, MultipartFile file) {
        return apiNaverOcr.callNaverOcr(message, file);
    }

    // 등기부등본 - 네이버 OCR 내용 추출
    public OcrDTO extractText(String message, MultipartFile file) {
        try {
            String rawJson = sendToNaverOcr(message, file);
            JsonNode root = objectMapper.readTree(rawJson);
            JsonNode fields = root.path("images").get(0).path("fields");

            StringBuilder fullTextBuilder = new StringBuilder();
            for (JsonNode field : fields) {
                fullTextBuilder.append(field.path("inferText").asText()).append(" ");
            }
            String fullText = fullTextBuilder.toString();

            System.out.println("전체 텍스트: " + fullText);

            // ] 와 [ 사이 추출 (정규표현식)
            List<String> extracted = new ArrayList<>();

            // 정규표현식 전략:
            // 표\s*제\s*부 : '표제부' 사이에 공백(\s*)이 몇 개든 찾음
            // .*?기\s*타\s*사\s*항 : 각 부 뒤에 '기타사항' 이후부터 찾음
            // [^가-힣a-zA-Z0-9]* : 문자와 숫자만 가져옴
            // (.*?) : 아무 문자나 모든 문자열, group(1)으로 가져옴
            // (?=갑\s*구|을\s*구|이\s*하\s*여\s*백|$) : 조건만 확인, 다음 섹션이나 '이하 여백'이 나오면 멈춤

            String[] sectionRegex = {
                    "표\\s*제\\s*부.*?기\\s*타\\s*사\\s*항[^가-힣a-zA-Z0-9]*(.*?)(?=갑\\s*구|을\\s*구|이\\s*하\\s*여\\s*백|$)",
                    "갑\\s*구.*?기\\s*타\\s*사\\s*항[^가-힣a-zA-Z0-9]*(.*?)(?=을\\s*구|이\\s*하\\s*여\\s*백|$)",
                    "을\\s*구.*?기\\s*타\\s*사\\s*항[^가-힣a-zA-Z0-9]*(.*?)(?=이\\s*하\\s*여\\s*백|$)"
            };

            for (String regex : sectionRegex) {
                Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(fullText);

                if (matcher.find()) {
                    String content = matcher.group(1).trim();
                    content = content.replaceFirst("^[^가-힣a-zA-Z0-9]*", "");
                    extracted.add(content);
                } else {
                    extracted.add("데이터를 찾을 수 없음");
                }
            }

            return new OcrDTO(extracted, rawJson);

        } catch (Exception e) {
            throw new RuntimeException("extractText: " + e.getMessage(), e);
        }
    }

    public RiskResponseDTO.RecordOfTitleResponse calculateRecordOfTitleScore(OcrDTO ocrData) {
        int score = 100;
        List<String> riskFactors = new ArrayList<>();
        String gapguIssue = "특이사항 없음";

        List<String> extracted = ocrData.extractedTexts();

        // 갑구 분석
        if (extracted.size() > 1) {
            String gapguText = extracted.get(1);

            if (gapguText.contains("경매") && !gapguText.contains("말소")) {
                score -= 100;
                riskFactors.add("경매절차 진행 중: 매우 위험");
                gapguIssue = "경매 개시 결정 확인";
            }
            if (gapguText.contains("가등기") && !gapguText.contains("말소")) {
                score -= 60;
                riskFactors.add("가등기 발견: 소유권 변동 가능성 있음");
            }
            if ((gapguText.contains("압류") || gapguText.contains("가압류")) && !gapguText.contains("말소")) {
                score -= 50;
                riskFactors.add("압류/가압류 확인: 채무 불이행 위험");
            }
            if (gapguText.contains("신탁") && !gapguText.contains("말소")) {
                score -= 30;
                riskFactors.add("신탁 등기: 실제 소유권자 확인 필요");
            }
        }

        // 을구 분석
        if (extracted.size() > 2) {
            String eulguText = extracted.get(2);

            if (eulguText.contains("임차권등기") && !eulguText.contains("말소")) {
                score -= 80;
                riskFactors.add("임차권등기명령 이력: 보증금 미반환 사고 전적 있음");
            }
            if (eulguText.contains("근저당권") && !eulguText.contains("말소")) {
                score -= 20;
                riskFactors.add("근저당권 설정: 담보대출 금액 확인 필요");
            }
        }

        score = Math.max(score, 0);

        return new RiskResponseDTO.RecordOfTitleResponse(gapguIssue, score, riskFactors);
    }
}