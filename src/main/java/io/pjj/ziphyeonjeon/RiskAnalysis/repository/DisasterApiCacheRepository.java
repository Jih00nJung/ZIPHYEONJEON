package io.pjj.ziphyeonjeon.RiskAnalysis.repository;

import io.pjj.ziphyeonjeon.RiskAnalysis.domain.DisasterApiCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DisasterApiCacheRepository extends JpaRepository<DisasterApiCache, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE DISASTER_API_CACHE", nativeQuery = true)
    void truncateTable();

    List<DisasterApiCache> findByAddressContaining(String address);
}