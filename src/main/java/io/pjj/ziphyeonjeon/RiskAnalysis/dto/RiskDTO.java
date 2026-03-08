package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

import java.util.List;

public record RiskDTO<T>(
        String status,
        String message,
        List<T> data

) {
}