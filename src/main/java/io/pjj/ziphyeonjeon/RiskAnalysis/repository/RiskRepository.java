package io.pjj.ziphyeonjeon.RiskAnalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.pjj.ziphyeonjeon.RiskAnalysis.domain.Risk;

@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {

}
