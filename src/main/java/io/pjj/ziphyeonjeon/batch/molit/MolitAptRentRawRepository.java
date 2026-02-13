package io.pjj.ziphyeonjeon.batch.molit;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MolitAptRentRawRepository extends JpaRepository<MolitAptRentRawEntity, Long> {
        List<MolitAptRentRawEntity> findBySigunguContainingAndRoadNameContainingOrderByContractYyyymmDescContractDayDesc(
                        String sigungu, String roadName);

        List<MolitAptRentRawEntity> findBySigunguContainingAndJibunContainingOrderByContractYyyymmDescContractDayDesc(
                        String sigungu,
                        String jibun);

        @Query("SELECT AVG(m.depositMan) FROM MolitAptRentRawEntity m " +
                        "WHERE m.sigungu = :sigungu " +
                        "AND m.eupmyeondong LIKE %:dong% " +
                        "AND m.exclusiveAreaM2 BETWEEN :minArea AND :maxArea")
        Double findAverageDeposit(@Param("sigungu") String sigungu,
                        @Param("dong") String dong,
                        @Param("minArea") BigDecimal minArea,
                        @Param("maxArea") BigDecimal maxArea);

        @Query("SELECT m.contractYyyymm, AVG(m.depositMan / m.exclusiveAreaM2) " +
                        "FROM MolitAptRentRawEntity m " +
                        "WHERE m.sigungu = :sigungu " +
                        "AND m.eupmyeondong LIKE %:dong% " +
                        "GROUP BY m.contractYyyymm " +
                        "ORDER BY m.contractYyyymm ASC")
        List<Object[]> findMonthlyAverageUnitPrice(@Param("sigungu") String sigungu, @Param("dong") String dong);
}
