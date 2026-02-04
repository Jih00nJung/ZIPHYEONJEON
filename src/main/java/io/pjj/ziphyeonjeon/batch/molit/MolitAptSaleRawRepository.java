package io.pjj.ziphyeonjeon.batch.molit;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MolitAptSaleRawRepository extends JpaRepository<MolitAptSaleRawEntity, Long> {
        // For P-001 Search
        List<MolitAptSaleRawEntity> findBySigunguContainingAndContractYyyymm(String sigungu, String contractYyyymm);

        // 도로명 검색 (ex: 테헤란로 123)
        List<MolitAptSaleRawEntity> findBySigunguAndRoadNameContainingOrderByContractYyyymmDescContractDayDesc(
                        String sigungu, String roadName);

        // 지번 검색 (ex: 역삼동 123-45) -> 아파트는 JIBUN 컬럼 사용
        List<MolitAptSaleRawEntity> findBySigunguAndJibunContainingOrderByContractYyyymmDescContractDayDesc(
                        String sigungu,
                        String jibun);

        @Query("SELECT AVG(m.dealAmountMan) FROM MolitAptSaleRawEntity m " +
                        "WHERE m.sigungu = :sigungu " +
                        "AND m.eupmyeondong LIKE %:dong% " +
                        "AND m.exclusiveAreaM2 BETWEEN :minArea AND :maxArea")
        Double findAverageDealAmount(@Param("sigungu") String sigungu,
                        @Param("dong") String dong,
                        @Param("minArea") BigDecimal minArea,
                        @Param("maxArea") BigDecimal maxArea);

        @Query("SELECT m.contractYyyymm, AVG(m.dealAmountMan / m.exclusiveAreaM2) " +
                        "FROM MolitAptSaleRawEntity m " +
                        "WHERE m.sigungu = :sigungu " +
                        "AND m.eupmyeondong LIKE %:dong% " +
                        "GROUP BY m.contractYyyymm " +
                        "ORDER BY m.contractYyyymm ASC")
        List<Object[]> findMonthlyAverageUnitPrice(@Param("sigungu") String sigungu, @Param("dong") String dong);
}
