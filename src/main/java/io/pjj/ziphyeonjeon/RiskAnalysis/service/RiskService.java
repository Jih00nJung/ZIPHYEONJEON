package io.pjj.ziphyeonjeon.RiskAnalysis.service;

import io.pjj.ziphyeonjeon.RiskAnalysis.domain.DisasterApiCache;
import io.pjj.ziphyeonjeon.RiskAnalysis.repository.DisasterApiCacheRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pjj.ziphyeonjeon.global.API.ApiDisaster;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskResponseDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.repository.RiskRepository;
import io.pjj.ziphyeonjeon.RiskAnalysis.domain.Risk;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiskService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RiskRepository riskRepository;
    private final DisasterApiCacheRepository cacheRepository;
    private final ApiDisaster apiDisaster;

    public RiskService(RiskRepository riskRepository, ApiDisaster apiDisaster,
                       DisasterApiCacheRepository cacheRepository) {
        this.riskRepository = riskRepository;
        this.cacheRepository = cacheRepository;
        this.apiDisaster = apiDisaster;
    }

    @Transactional
    public void refreshApiCache() {
        try {
            String rawData = apiDisaster.fetchAllDisasterData();

            JsonNode root = objectMapper.readTree(rawData);
            JsonNode items = root.get("body");

            if (items.isMissingNode() || !items.isArray()) {
                System.out.println("파싱 오류: 데이터가 존재하지 않거나 배열 형식이 아닙니다.");
                return;
            }

            cacheRepository.truncateTable();

            List<DisasterApiCache> cacheList = new ArrayList<>();
            for (JsonNode item : items) {
                DisasterApiCache entity = new DisasterApiCache(
                        item.path("DST_RSK_RGN_RPRS_ADDR").asText("주소 없음"),
                        item.path("LAT").asText("0.0"),
                        item.path("LOT").asText("0.0"),
                        item.path("TYPE").asText("미지정"),
                        item.path("DSGN_RSN").asText("내용 없음"),
                        item.path("REG_DT").asText("")
                );
                cacheList.add(entity);
            }

            cacheRepository.saveAll(cacheList);
            System.out.println("성공: " + cacheList.size() + "건의 재난 API 데이터가 동기화되었습니다.");

        } catch (Exception e) {
            throw new RuntimeException("API 데이터 파싱 및 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // DB 조회 + 필터링
    @Transactional(readOnly = true)
    public RiskResponseDTO searchAddress(String address) {
        List<DisasterApiCache> entities = cacheRepository.findByAddressContaining(address);

        List<RiskResponseDTO.DisasterResponse> dtoList = entities.stream()
                .map(entity -> new RiskResponseDTO.DisasterResponse(
                        entity.getAddress(),
                        entity.getLat(),
                        entity.getLng(),
                        entity.getType(),
                        entity.getDesignReason(),
                        entity.getRegTime()
                ))
                .collect(Collectors.toList());

        return new RiskResponseDTO(
                "SUCCESS",
                entities.isEmpty() ? "검색 결과가 없습니다." : "조회 성공",
                dtoList
        );
    }

    // 점수 저장
    public Risk scoreSave(Long propertyId, String disasterGrade) {
        BigDecimal score = calculateScore(disasterGrade);
        String finalGrade = (score.compareTo(new BigDecimal("70")) >= 0) ? "안전" : "위험";

        Risk risk = new Risk(
                propertyId,
                score,
                finalGrade,
                disasterGrade);
        return riskRepository.save(risk);
    }

    // 점수 계산
    private BigDecimal calculateScore(String grade) {
        if ("안전".equals(grade))
            return new BigDecimal("100.00");
        if ("주의".equals(grade))
            return new BigDecimal("50.00");
        return new BigDecimal("20.00");
    }

}
