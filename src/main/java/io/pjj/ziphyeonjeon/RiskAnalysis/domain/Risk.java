package io.pjj.ziphyeonjeon.RiskAnalysis.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RISK_ANALYSIS_RESULT")
public class Risk {

    protected Risk() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RISK_RESULT_ID")
    private Long riskResultId;

    @Column(name = "PROPERTY_ID", nullable = false)
    private Long propertyId;

    @Column(name = "ANALYZED_AT", nullable = false)
    private LocalDateTime analyzedAt;

    @Column(name = "TOTAL_SAFETY_SCORE", precision = 5, scale = 2)
    private BigDecimal totalSafetyScore;

    @Column(name = "FINAL_GRADE", length = 20)
    private String finalGrade;

    @Column(name = "REGISTER_RISK_SCORE", precision = 5, scale = 2)
    private BigDecimal registerRiskScore;

    @Column(name = "JEONSE_RISK_SCORE", precision = 5, scale = 2)
    private BigDecimal jeonseRiskScore;

    @Column(name = "DISASTER_RISK_GRADE", length = 20)
    private String disasterRiskGrade;

    @Column(name = "LEGAL_ANALYSIS_RESULT", columnDefinition = "json")
    private String legalAnalysisResult;

    // 생성자
    public Risk(Long propertyId, BigDecimal totalSafetyScore, String finalGrade, String disasterRiskGrade) {
        this.propertyId = propertyId;
        this.totalSafetyScore = totalSafetyScore;
        this.finalGrade = finalGrade;
        this.disasterRiskGrade = disasterRiskGrade;
        this.analyzedAt = LocalDateTime.now();
    }

    // Getter
    public Long getRiskResultId() {
        return riskResultId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public BigDecimal getTotalSafetyScore() {
        return totalSafetyScore;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public BigDecimal getRegisterRiskScore() {
        return registerRiskScore;
    }

    public BigDecimal getJeonseRiskScore() {
        return jeonseRiskScore;
    }

    public String getDisasterRiskGrade() {
        return disasterRiskGrade;
    }

    public String getLegalAnalysisResult() {
        return legalAnalysisResult;
    }

}