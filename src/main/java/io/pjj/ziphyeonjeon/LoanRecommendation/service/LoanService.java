package io.pjj.ziphyeonjeon.LoanRecommendation.service;

import io.pjj.ziphyeonjeon.LoanRecommendation.dto.LoanDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.service.RiskApiService;
import io.pjj.ziphyeonjeon.global.API.ApiLoan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LoanService {
    private final ApiLoan apiLoan;
    private final RiskApiService riskApiService;

    public LoanService(ApiLoan apiLoan, RiskApiService riskApiService) {
        this.apiLoan = apiLoan;
        this.riskApiService = riskApiService;
    }

    // 대출 API
    public List<LoanDTO> requestLoanApi() {
        Map<String, String> rawData = apiLoan.fetchAllLoanData();
        List<LoanDTO> allLoanList = new ArrayList<>();

        List<String> allowedCategories = List.of("준정부기관", "공공기관", "지자체");

        rawData.forEach((key, json) -> {
            List<LoanDTO> loanList = riskApiService.extractApiData(
                    json,
                    LoanDTO.class,
                    "response", "body", "items", "item"
            );

            List<LoanDTO> filteredList = loanList.stream()
                    .filter(loan -> allowedCategories.stream()
                            .anyMatch(allowed -> loan.instCtg() != null && loan.instCtg().contains(allowed)))
                    .toList();
            allLoanList.addAll(filteredList);
        });
        return allLoanList.stream().distinct().toList();
    }
}
