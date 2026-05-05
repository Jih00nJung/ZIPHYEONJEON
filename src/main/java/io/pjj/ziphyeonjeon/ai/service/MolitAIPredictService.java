package io.pjj.ziphyeonjeon.ai.service;

import io.pjj.ziphyeonjeon.ai.dto.PredictRequestDto;
import io.pjj.ziphyeonjeon.ai.dto.PredictResponseDto;
import io.pjj.ziphyeonjeon.ai.entity.Analysis;
import io.pjj.ziphyeonjeon.ai.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MolitAIPredictService {

    private final AnalysisRepository analysisRepository;
    private final io.pjj.ziphyeonjeon.PriceSearch.repository.HouseRepository houseRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.python.api.url:http://localhost:8000}")
    private String pythonApiUrl;

    /**
     * [하이브리드 캐싱 적용] Python AI 모델에 예측을 요청하고 결과를 DB에 저장합니다.
     */
    @Transactional
    public Analysis predictAndSave(Long userId, Long houseId, String propertyType, String dealType, String sigungu, String targetMonth, List<Map<String, Object>> features) {
        
        // 1. 하이브리드 캐싱: 최근 1개월 이내 동일 지역/유형 분석 결과가 있는지 스캔
        java.time.LocalDateTime oneMonthAgo = java.time.LocalDateTime.now().minusMonths(1);
        java.util.Optional<Analysis> cachedResult = analysisRepository.findFirstBySigunguAndPropertyTypeAndDealTypeAndCreatedAtAfterOrderByCreatedAtDesc(
                sigungu, propertyType, dealType, oneMonthAgo);

        int month = Integer.parseInt(targetMonth.replace("h", "").replace("m", ""));
        
        if (cachedResult.isPresent()) {
            // [Cache Hit] 파이썬 서버 호출 스킵! 기존 값을 복사하여 이 유저의 새 기록으로 저장
            log.info("[Hybrid Cache Hit] Reusing AI prediction for sigungu: {}", sigungu);
            Analysis newRecord = new Analysis();
            newRecord.setUserId(userId);
            newRecord.setHouseId(houseId);
            newRecord.setPropertyType(propertyType);
            newRecord.setDealType(dealType);
            newRecord.setSigungu(sigungu);
            newRecord.setPredictTargetMonth(month);
            newRecord.setPredictedPrice(cachedResult.get().getPredictedPrice());
            newRecord.setTrendPercentage(cachedResult.get().getTrendPercentage());
            return analysisRepository.save(newRecord);
        }

        // [Cache Miss] 최근 1개월 내 기록이 없으므로 파이썬 서버 실제 호출
        log.info("[Hybrid Cache Miss] Requesting fresh AI Prediction from Python API for sigungu: {}", sigungu);
        
        PredictRequestDto requestDto = new PredictRequestDto(targetMonth, features);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PredictRequestDto> entity = new HttpEntity<>(requestDto, headers);

        String apiDealType = dealType.contains("매매") ? "sale" : "rent";
        String url = pythonApiUrl + "/predict/" + apiDealType;
        
        try {
            ResponseEntity<PredictResponseDto> response = restTemplate.postForEntity(url, entity, PredictResponseDto.class);
            PredictResponseDto responseDto = response.getBody();
            
            if (responseDto != null && responseDto.getPredictions() != null && !responseDto.getPredictions().isEmpty()) {
                Double predictedVal = responseDto.getPredictions().get(0);
                
                // [NEW] 추세율(Trend Percentage) 계산을 위해 현재 시군구 평균가 조회
                Double currentAvgPrice = houseRepository.findAveragePriceBySigunguAndPropertyType(sigungu, propertyType, dealType);
                BigDecimal trendPercentage = BigDecimal.ZERO;
                if (currentAvgPrice != null && currentAvgPrice > 0) {
                    // ((예측가 - 현재가) / 현재가) * 100
                    double trend = ((predictedVal - currentAvgPrice) / currentAvgPrice) * 100.0;
                    trendPercentage = BigDecimal.valueOf(trend).setScale(2, java.math.RoundingMode.HALF_UP);
                }
                
                Analysis analysis = new Analysis();
                analysis.setUserId(userId);
                analysis.setHouseId(houseId);
                analysis.setPropertyType(propertyType);
                analysis.setDealType(dealType);
                analysis.setSigungu(sigungu);
                analysis.setPredictTargetMonth(month);
                analysis.setPredictedPrice(BigDecimal.valueOf(predictedVal));
                analysis.setTrendPercentage(trendPercentage);
                
                return analysisRepository.save(analysis);
            } else {
                log.error("AI Prediction returned empty result.");
                throw new RuntimeException("AI API 응답이 비어있습니다.");
            }
            
        } catch (Exception e) {
            log.error("Failed to fetch prediction from Python API", e);
            throw new RuntimeException("AI 예측 서버 통신 실패", e);
        }
    }

    /**
     * [마이페이지 용] 특정 유저의 분석 내역을 최신순으로 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<Analysis> getMyPredictions(Long userId) {
        return analysisRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
