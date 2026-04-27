package io.pjj.ziphyeonjeon.PriceSearch.repository;

import io.pjj.ziphyeonjeon.PriceSearch.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {

    // 주소 기반 일반 검색
    List<House> findBySigunguContainingAndRoadnameContainingOrderByContractYmDescContractDayDesc(String sigungu, String roadname);

    List<House> findBySigunguContainingAndJibunContainingOrderByContractYmDescContractDayDesc(String sigungu, String jibun);

    // 동 + 본번을 통한 Loose 검색 (Jibun 정보 부재시)
    List<House> findBySigunguContainingAndEmdContainingAndBonbunContainingOrderByContractYmDescContractDayDesc(String sigungu, String emd, String bonbun);

    // 단지명(Name) 기반 검색 (2024년 이후 등)
    List<House> findByNameContainingAndContractYmGreaterThanEqualOrderByContractYmDescContractDayDesc(String name, String contractYm);

    // 시군구와 계약년월 기반 검색
    List<House> findBySigunguContainingAndContractYm(String sigungu, String contractYm);


    // 평균 거래금액(매매 등) - AREA 범위와 타입 지정
    @Query("SELECT AVG(h.trade) FROM House h " +
            "WHERE h.sigungu LIKE %:sigungu% " +
            "AND (h.emd LIKE %:dong% OR h.sigungu LIKE %:dong%) " +
            "AND h.area BETWEEN :minArea AND :maxArea " +
            "AND (:propertyType IS NULL OR h.propertyType = :propertyType) " +
            "AND h.dealType = '매매'")
    Double findAverageTrade(@Param("sigungu") String sigungu,
                            @Param("dong") String dong,
                            @Param("minArea") BigDecimal minArea,
                            @Param("maxArea") BigDecimal maxArea,
                            @Param("propertyType") String propertyType);

    // 평균 전세보증금 - AREA 범위와 타입 지정
    @Query("SELECT AVG(h.deposit) FROM House h " +
            "WHERE h.sigungu LIKE %:sigungu% " +
            "AND (h.emd LIKE %:dong% OR h.sigungu LIKE %:dong%) " +
            "AND h.area BETWEEN :minArea AND :maxArea " +
            "AND (:propertyType IS NULL OR h.propertyType = :propertyType) " +
            "AND h.dealType = '전세'")
    Double findAverageDeposit(@Param("sigungu") String sigungu,
                              @Param("dong") String dong,
                              @Param("minArea") BigDecimal minArea,
                              @Param("maxArea") BigDecimal maxArea,
                              @Param("propertyType") String propertyType);

    // 월별 평균 단가 (매매) - 단위 면적당 가격
    @Query("SELECT h.contractYm, AVG(h.trade / h.area) " +
            "FROM House h " +
            "WHERE h.sigungu LIKE %:sigungu% " +
            "AND (h.emd LIKE %:dong% OR h.sigungu LIKE %:dong%) " +
            "AND h.propertyType = :propertyType " +
            "AND h.dealType = '매매' " +
            "GROUP BY h.contractYm " +
            "ORDER BY h.contractYm ASC")
    List<Object[]> findMonthlyAverageTradeUnitPrice(@Param("sigungu") String sigungu, @Param("dong") String dong, @Param("propertyType") String propertyType);

    // 월별 평균 단가 (전세) - 단위 면적당 가격
    @Query("SELECT h.contractYm, AVG(h.deposit / h.area) " +
            "FROM House h " +
            "WHERE h.sigungu LIKE %:sigungu% " +
            "AND (h.emd LIKE %:dong% OR h.sigungu LIKE %:dong%) " +
            "AND h.propertyType = :propertyType " +
            "AND h.dealType = '전세' " +
            "GROUP BY h.contractYm " +
            "ORDER BY h.contractYm ASC")
    List<Object[]> findMonthlyAverageJeonseUnitPrice(@Param("sigungu") String sigungu, @Param("dong") String dong, @Param("propertyType") String propertyType);

    // 월별 평균 단가 (월세) - 보증금 제외한 순수 월세액
    @Query("SELECT h.contractYm, AVG(h.rentfee) " +
            "FROM House h " +
            "WHERE h.sigungu LIKE %:sigungu% " +
            "AND (h.emd LIKE %:dong% OR h.sigungu LIKE %:dong%) " +
            "AND h.propertyType = :propertyType " +
            "AND h.dealType = '월세' " +
            "GROUP BY h.contractYm " +
            "ORDER BY h.contractYm ASC")
    List<Object[]> findMonthlyAverageWolseAmount(@Param("sigungu") String sigungu, @Param("dong") String dong, @Param("propertyType") String propertyType);
}
