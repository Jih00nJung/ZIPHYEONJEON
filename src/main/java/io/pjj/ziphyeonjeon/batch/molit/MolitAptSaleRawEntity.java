package io.pjj.ziphyeonjeon.batch.molit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MOLIT_APT_SALE_RAW",
        indexes = {
                @Index(name = "IDX_APT_SALE_YYYYMM", columnList = "CONTRACT_YYYYMM"),
                @Index(name = "IDX_APT_SALE_SIGUNGU", columnList = "SIGUNGU"),
                @Index(name = "IDX_APT_SALE_COMPLEX", columnList = "COMPLEX_NAME")
        })
public class MolitAptSaleRawEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    // 적재 메타
    @Column(name = "DATA_YEAR", nullable = false)
    private Integer dataYear;

    @Column(name = "DATA_MONTH", nullable = false)
    private Integer dataMonth;

    @Column(name = "SOURCE_FILE")
    private String sourceFile;

    @Column(name = "INGESTED_AT", nullable = false)
    private LocalDateTime ingestedAt = LocalDateTime.now();

    // 위치
    @Column(name = "SIDO")
    private String sido;

    @Column(name = "SIGUNGU")
    private String sigungu;

    @Column(name = "EUPMYEONDONG")
    private String eupmyeondong;

    @Column(name = "JIBUN")
    private String jibun;

    @Column(name = "BONBUN")
    private String bonbun;

    @Column(name = "BUBUN")
    private String bubun;

    @Column(name = "ROAD_NAME")
    private String roadName;

    // 단지/건물
    @Column(name = "COMPLEX_NAME")
    private String complexName;

    @Column(name = "EXCLUSIVE_AREA_M2", precision = 10, scale = 4)
    private BigDecimal exclusiveAreaM2;

    @Column(name = "FLOOR_NO")
    private Integer floorNo;

    @Column(name = "BUILT_YEAR")
    private Integer builtYear;

    // 계약/거래
    @Column(name = "CONTRACT_YYYYMM")
    private String contractYyyymm; // 202401

    @Column(name = "CONTRACT_DAY")
    private Integer contractDay;

    @Column(name = "DEAL_AMOUNT_MAN")
    private Long dealAmountMan;

    // 기타
    @Column(name = "CANCEL_DATE")
    private String cancelDate; // YYYYMMDD

    @Column(name = "DEAL_TYPE")
    private String dealType;

    @Column(name = "BROKER_LOCATION")
    private String brokerLocation;

    @Column(name = "REGISTRATION_DATE")
    private String registrationDate; // YYYYMMDD

    @Column(name = "BUYER_TYPE")
    private String buyerType;

    @Column(name = "SELLER_TYPE")
    private String sellerType;
}
