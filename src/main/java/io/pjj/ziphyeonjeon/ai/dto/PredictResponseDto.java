package io.pjj.ziphyeonjeon.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class PredictResponseDto {
    private String dealType;
    private String targetMonth;
    private List<Double> predictions;
}
