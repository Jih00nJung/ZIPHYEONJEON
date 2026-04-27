package io.pjj.ziphyeonjeon.ai.repository;

import io.pjj.ziphyeonjeon.ai.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    
    // 지역과 주택/거래 유형에 따른 분석 결과 조회 (프론트에서 사용)
    List<Analysis> findBySigunguAndPropertyTypeAndDealTypeOrderByCreatedAtDesc(String sigungu, String propertyType, String dealType);

}
