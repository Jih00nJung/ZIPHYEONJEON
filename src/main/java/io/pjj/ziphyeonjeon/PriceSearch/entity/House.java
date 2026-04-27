package io.pjj.ziphyeonjeon.PriceSearch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "HOUSE",
        indexes = {
                @Index(name = "IDX_HOUSE_SIGUNGU", columnList = "SIGUNGU"),
                @Index(name = "IDX_HOUSE_CONTRACT_YM", columnList = "CONTRACT_YM"),
                @Index(name = "IDX_HOUSE_NAME", columnList = "NAME")
        })
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSE_ID")
    private Long houseId;

    @Column(name = "PROPERTY_TYPE", length = 20)
    private String propertyType; // 아파트, 연립다세대, 오피스텔 등

    @Column(name = "DEAL_TYPE", length = 20)
    private String dealType; // 매매, 전세, 월세 등

    @Column(name = "SIDO", length = 30)
    private String sido;

    @Column(name = "SIGUNGU", length = 60)
    private String sigungu;

    @Column(name = "EMD", length = 30)
    private String emd;

    @Column(name = "ROADNAME", length = 150)
    private String roadname;

    @Column(name = "JIBUN", length = 50)
    private String jibun;

    @Column(name = "BONBUN", length = 20)
    private String bonbun;

    @Column(name = "BUBUN", length = 20)
    private String bubun;

    @Column(name = "AREA", precision = 14, scale = 4)
    private BigDecimal area;

    @Column(name = "CONTRACT_YM", length = 10)
    private String contractYm;

    @Column(name = "CONTRACT_DAY")
    private Integer contractDay;

    @Column(name = "TRADE")
    private Long trade;

    @Column(name = "DEPOSIT")
    private Long deposit;

    @Column(name = "RENTFEE")
    private Long rentfee;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "FLOOR_NO")
    private Integer floorNo;

    @Column(name = "SOURCE_TABLE", length = 50)
    private String sourceTable;

    @Column(name = "SOURCE_RAW_ID")
    private Long sourceRawId;
}
