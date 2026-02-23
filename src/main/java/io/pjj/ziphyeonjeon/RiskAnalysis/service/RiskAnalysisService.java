package io.pjj.ziphyeonjeon.RiskAnalysis.service;

import io.pjj.ziphyeonjeon.RiskAnalysis.dto.DisasterDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import io.pjj.ziphyeonjeon.RiskAnalysis.dto.OcrDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.BuildingDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskDTO;

@Service
public class RiskAnalysisService {

    private final RiskApiService riskApiService;
    private final RiskOcrService riskOcrService;

    public RiskAnalysisService(RiskApiService riskApiService, RiskOcrService riskOcrService) {
        this.riskApiService = riskApiService;
        this.riskOcrService = riskOcrService;
    }

    // 재해 위험도 분석
    public RiskDTO<DisasterDTO.DisasterResponse> analyzeDisasterRisk(String address) {
        List<DisasterDTO> rawData = riskApiService.requestDisasterApi(address);

        int score = calculateDisasterScore(rawData);

        DisasterDTO.DisasterResponse responseContent = new DisasterDTO.DisasterResponse(
                address,
                score,
                rawData
        );

        return new RiskDTO<>(
                "success",
                "/RiskAnalysisService/Disaster, 주소: " + address,
                List.of(responseContent)
        );
    }

    // 재해 위험 점수 계산
    private int calculateDisasterScore(List<DisasterDTO> disasterData) {
        int score = 100;

        // 감점 요인
        if (!disasterData.isEmpty()) {
            score -= (5 * disasterData.size());
        }

        return Math.max(score, 0);
    }


    // 건축물대장 위험도 분석
    public RiskDTO<BuildingDTO.BuildingResponse> analyzeBuildingRisk(String address) {
        List<BuildingDTO> buildingData = riskApiService.requestBuildingApi(address);

        int housePrice = buildingData.isEmpty() ? 0 : buildingData.getLast().hsprc();
        int creationDay = buildingData.isEmpty() ? 0 : buildingData.getLast().crtnDay();

        List<String> reasons = new ArrayList<>();
        int score = calculateBuildingScore(buildingData, reasons);

        return new RiskDTO<>("success", "/RiskAnalysisService/Building, 주소: " + address,
                List.of(new BuildingDTO.BuildingResponse(
                        address,
                        housePrice,
                        score,
                        BuildingDTO.BuildingResponse.calculateBuildingLevel(score),
                        reasons,
                        creationDay
                ))
        );
    }

    // 건축물대장 위험 점수 계산
    private int calculateBuildingScore(List<BuildingDTO> titles, List<String> factors) {
        int score = 100;
        if (titles.isEmpty()) {
            factors.add("조회된 건축물 정보가 없습니다.");
            return 100;
        }

        BuildingDTO title = titles.getFirst();

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
    public RiskDTO<OcrDTO.RecordOfTitleResponse> analyzeRecordOfTitleRisk(String message, MultipartFile file) {
        OcrDTO ocrData = riskOcrService.requestOcrApi(message, file);
        OcrDTO.RecordOfTitleResponse RecordOfTitleResult = calculateRecordOfTitleScore(ocrData);

        return new RiskDTO<>("success", "/RiskAnalysisService/RecordOfTitle", List.of(RecordOfTitleResult));
    }

    // 등기부등본 점수 계산
    public OcrDTO.RecordOfTitleResponse calculateRecordOfTitleScore(OcrDTO ocrData) {
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

        return new OcrDTO.RecordOfTitleResponse(gapguIssue, score, riskFactors);
    }
}