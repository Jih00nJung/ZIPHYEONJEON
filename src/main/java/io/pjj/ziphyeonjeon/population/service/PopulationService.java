package io.pjj.ziphyeonjeon.population.service;

import io.pjj.ziphyeonjeon.population.dto.PopulationResponse;
import java.util.List;

public interface PopulationService {
    void syncLocalCsvFiles();
    
    List<PopulationResponse> getPopulationByAdstrdCd(String adstrdCd, String referenceDate);
}
