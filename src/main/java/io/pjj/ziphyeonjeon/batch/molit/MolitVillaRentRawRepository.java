package io.pjj.ziphyeonjeon.batch.molit;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MolitVillaRentRawRepository extends JpaRepository<MolitVillaRentRawEntity, Long> {
        List<MolitVillaRentRawEntity> findBySigunguContainingAndRoadNameContainingOrderByContractYmDescContractDayDesc(
                        String sigungu,
                        String roadName);

        List<MolitVillaRentRawEntity> findBySigunguContainingAndBeonjiContainingOrderByContractYmDescContractDayDesc(
                        String sigungu,
                        String beonji);

        @Query("SELECT AVG(m.depositMan) FROM MolitVillaRentRawEntity m " +
                        "WHERE m.sigungu = :sigungu " +
                        "AND m.beonji LIKE %:dong% " +
                        "AND m.exclArea BETWEEN :minArea AND :maxArea")
        Double findAverageDeposit(@Param("sigungu") String sigungu,
                        @Param("dong") String dong,
                        @Param("minArea") BigDecimal minArea,
                        @Param("maxArea") BigDecimal maxArea);

        @Query("SELECT m.contractYm, AVG(m.depositMan / m.exclArea) " +
                        "FROM MolitVillaRentRawEntity m " +
                        "WHERE m.sigungu = :sigungu " +
                        "AND m.beonji LIKE %:dong% " +
                        "GROUP BY m.contractYm " +
                        "ORDER BY m.contractYm ASC")
        List<Object[]> findMonthlyAverageUnitPrice(@Param("sigungu") String sigungu, @Param("dong") String dong);
}
