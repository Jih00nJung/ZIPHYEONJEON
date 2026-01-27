package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OcrDTO(
        List<String> extractedTexts,    // ], [
        String rawResult                // 원본
) {
}
