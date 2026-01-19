package io.pjj.ziphyeonjeon.RiskAnalysis.repository;

import io.pjj.ziphyeonjeon.RiskAnalysis.domain.DisasterCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DisasterCacheRepository extends JpaRepository<DisasterCache, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE DISASTER_CACHE", nativeQuery = true)
    void truncateTable();

    List<DisasterCache> findByAddressContaining(String address);
}