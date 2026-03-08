package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OcrDTO(
        List<String> extractedTexts,
        String rawResult                // 원본
) {
    public record RecordOfTitleResponse(
            String gapguIssue,
            Integer score,
            List<String> riskFactors

    ) {
        public static String calculateRecordOfTitleLevel(Integer score) {
            if (score >= 90) return "안전";
            if (score >= 70) return "주의";
            return "위험";
        }
    }
}
