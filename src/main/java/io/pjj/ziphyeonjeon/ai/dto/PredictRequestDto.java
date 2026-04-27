package io.pjj.ziphyeonjeon.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictRequestDto {
    private String targetMonth; // "h1m", "h3m", "h6m"
    private List<Map<String, Object>> features;
}
