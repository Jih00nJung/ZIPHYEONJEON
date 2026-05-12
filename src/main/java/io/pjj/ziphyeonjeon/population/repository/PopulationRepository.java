package io.pjj.ziphyeonjeon.population.repository;

import io.pjj.ziphyeonjeon.population.entity.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PopulationRepository
        extends JpaRepository<Population, Long>, PopulationRepositoryCustom {
    @Query("SELECT p FROM Population p WHERE p.adstrdCd = :adstrdCd ORDER BY p.referenceDate ASC, p.hours ASC")
    List<Population> findByAdstrdCd(@Param("adstrdCd") String adstrdCd);

    @Query("SELECT p FROM Population p WHERE p.adstrdCd = :adstrdCd AND p.referenceDate = :refDate ORDER BY p.hours ASC")
    List<Population> findByAdstrdCdAndDate(@Param("adstrdCd") String adstrdCd, @Param("refDate") Timestamp refDate);
}
