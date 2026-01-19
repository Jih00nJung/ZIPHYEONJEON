package io.pjj.ziphyeonjeon.batch.molit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MOLIT_OFFICETEL_SALE_RAW", indexes = {
        @Index(name = "IDX_OS_YM", columnList = "CONTRACT_YM"),
        @Index(name = "IDX_OS_LOC", columnList = "SIGUNGU, ROAD_NAME"),
        @Index(name = "IDX_OS_COMPLEX", columnList = "COMPLEX_NAME, EXCL_AREA")
})
public class MolitOfficetelSaleRawEntity {

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

    // 원본 컬럼
    @Column(name = "NO_NUM")
    private Integer noNum;

    @Column(name = "SIGUNGU")
    private String sigungu;

    @Column(name = "BEONJI")
    private String beonji;

    @Column(name = "BONBUN")
    private String bonbun;

    @Column(name = "BUBUN")
    private String bubun;

    @Column(name = "COMPLEX_NAME")
    private String complexName;

    @Column(name = "EXCL_AREA", precision = 10, scale = 3)
    private BigDecimal exclArea;

    @Column(name = "CONTRACT_YM")
    private Integer contractYm;

    @Column(name = "CONTRACT_DAY")
    private Integer contractDay;

    @Column(name = "DEAL_AMOUNT_MAN")
    private Long dealAmountMan;

    @Column(name = "FLOOR_NO")
    private Integer floorNo;

    @Column(name = "BUYER_TYPE")
    private String buyerType;

    @Column(name = "SELLER_TYPE")
    private String sellerType;

    @Column(name = "BUILT_YEAR")
    private Integer builtYear;

    @Column(name = "ROAD_NAME")
    private String roadName;

    @Column(name = "CANCEL_REASON")
    private String cancelReason;

    @Column(name = "DEAL_TYPE")
    private String dealType;

    @Column(name = "BROKER_LOC")
    private String brokerLoc;
}
