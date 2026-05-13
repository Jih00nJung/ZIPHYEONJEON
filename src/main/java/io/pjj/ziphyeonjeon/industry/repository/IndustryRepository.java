package io.pjj.ziphyeonjeon.industry.repository;

import io.pjj.ziphyeonjeon.industry.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long>, IndustryRepositoryCustom {
    List<Industry> findByAdstrdCd(String adstrdCd);
    
    @Query("SELECT i FROM Industry i WHERE i.svcIndutyNm LIKE %:svcIndutyNm% ORDER BY i.adstrdCd ASC")
    List<Industry> findBySvcIndutyNm(@Param("svcIndutyNm") String svcIndutyNm);
}
