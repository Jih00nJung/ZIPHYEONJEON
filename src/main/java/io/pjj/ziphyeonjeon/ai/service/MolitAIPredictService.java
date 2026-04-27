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
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.python.api.url:http://localhost:8000}")
    private String pythonApiUrl;

    /**
     * Python AI 모델에 예측을 요청하고 결과를 DB에 저장합니다.
     */
    @Transactional
    public Analysis predictAndSave(String propertyType, String dealType, String sigungu, String targetMonth, List<Map<String, Object>> features) {
        
        // 1. Python API에 보낼 JSON 페이로드 구성
        PredictRequestDto requestDto = new PredictRequestDto(targetMonth, features);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PredictRequestDto> entity = new HttpEntity<>(requestDto, headers);

        // 2. Python FastAPI 호출 (dealType을 매매(sale)와 전월세(rent)로 영문명 변환 필요)
        String apiDealType = dealType.contains("매매") ? "sale" : "rent";
        String url = pythonApiUrl + "/predict/" + apiDealType;
        
        log.info("Requesting AI Prediction to URL: {}", url);
        
        try {
            ResponseEntity<PredictResponseDto> response = restTemplate.postForEntity(url, entity, PredictResponseDto.class);
            PredictResponseDto responseDto = response.getBody();
            
            if (responseDto != null && responseDto.getPredictions() != null && !responseDto.getPredictions().isEmpty()) {
                
                // 단건 예측이라고 가정 (리스트의 첫 번째 값 사용)
                Double predictedVal = responseDto.getPredictions().get(0);
                
                // 3. ANALYSIS 테이블에 결과 저장
                Analysis analysis = new Analysis();
                analysis.setPropertyType(propertyType);
                analysis.setDealType(dealType);
                analysis.setSigungu(sigungu);
                
                // 타겟 개월 수 파싱 (e.g., "h3m" -> 3)
                int month = Integer.parseInt(targetMonth.replace("h", "").replace("m", ""));
                analysis.setPredictTargetMonth(month);
                
                analysis.setPredictedPrice(BigDecimal.valueOf(predictedVal));
                
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
}
