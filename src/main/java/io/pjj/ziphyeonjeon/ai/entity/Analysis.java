package io.pjj.ziphyeonjeon.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ANALYSIS", indexes = {
        @Index(name = "idx_analysis_property_deal", columnList = "PROPERTY_TYPE, DEAL_TYPE"),
        @Index(name = "idx_analysis_sigungu", columnList = "SIGUNGU")
})
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANALYSIS_ID")
    private Long analysisId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "HOUSE_ID")
    private Long houseId;

    @Column(name = "STORE_ID")
    private Long storeId;

    // 아파트, 연립다세대, 오피스텔, 상가 등 (상가 개발자 합류 대비)
    @Column(name = "PROPERTY_TYPE", length = 30)
    private String propertyType;

    // 매매, 전월세, 전세, 월세 등
    @Column(name = "DEAL_TYPE", length = 30)
    private String dealType;

    // 해당 지역 
    @Column(name = "SIGUNGU", length = 100)
    private String sigungu;

    // 예측 대상 개월 수 (1, 3, 6 등)
    @Column(name = "PREDICT_TARGET_MONTH")
    private Integer predictTargetMonth;

    // 역로그 환산이 완료된 실제 예측 가격 (원 또는 만원 단위)
    @Column(name = "PREDICTED_PRICE", precision = 20, scale = 4)
    private BigDecimal predictedPrice;

    // 기타 참조를 위한 메타 데이터 필드 (예: 모델 버전, 기준 연월 등)
    @Column(name = "REFERENCE_YM", length = 10)
    private String referenceYm;

    // [NEW] 추세율(상승/하락 퍼센티지)
    @Column(name = "TREND_PERCENTAGE", precision = 5, scale = 2)
    private BigDecimal trendPercentage;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
