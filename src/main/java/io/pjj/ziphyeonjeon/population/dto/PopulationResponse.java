package io.pjj.ziphyeonjeon.population.dto;

public record PopulationResponse(
        String REFERENCE_DATE,
        String ADSTRD_CD,
        float POPULATION_COUNT,
        int HOURS
) {
}
