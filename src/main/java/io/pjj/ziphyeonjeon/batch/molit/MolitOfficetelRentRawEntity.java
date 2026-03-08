package io.pjj.ziphyeonjeon.batch.molit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MOLIT_OFFICETEL_RENT_RAW", indexes = {
        @Index(name = "IDX_OR_YM", columnList = "CONTRACT_YM"),
        @Index(name = "IDX_OR_LOC", columnList = "SIGUNGU, ROAD_NAME"),
        @Index(name = "IDX_OR_COMPLEX", columnList = "COMPLEX_NAME, EXCL_AREA")
})
public class MolitOfficetelRentRawEntity {

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

    @Column(name = "RENT_TYPE")
    private String rentType;

    @Column(name = "EXCL_AREA", precision = 10, scale = 3)
    private BigDecimal exclArea;

    @Column(name = "CONTRACT_YM")
    private Integer contractYm;

    @Column(name = "CONTRACT_DAY")
    private Integer contractDay;

    @Column(name = "DEPOSIT_MAN")
    private Long depositMan;

    @Column(name = "MONTHLY_RENT_MAN")
    private Long monthlyRentMan;

    @Column(name = "FLOOR_NO")
    private Integer floorNo;

    @Column(name = "BUILT_YEAR")
    private Integer builtYear;

    @Column(name = "ROAD_NAME")
    private String roadName;

    @Column(name = "CONTRACT_PERIOD")
    private String contractPeriod;

    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    @Column(name = "RENEWAL_REQ_RIGHT")
    private String renewalReqRight;

    @Column(name = "PREV_CONTRACT")
    private String prevContract;

    @Column(name = "PREV_MONTHLY_RENT_MAN")
    private Long prevMonthlyRentMan;
}
