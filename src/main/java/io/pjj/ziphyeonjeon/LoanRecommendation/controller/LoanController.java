package io.pjj.ziphyeonjeon.LoanRecommendation.controller;

import io.pjj.ziphyeonjeon.LoanRecommendation.dto.LoanDTO;
import io.pjj.ziphyeonjeon.LoanRecommendation.service.LoanService;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.BuildingDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.service.RiskAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loan")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // 대출 목록 로드
    @GetMapping("/list")
    public ResponseEntity<List<LoanDTO>> getLoan() {
        System.out.println("/api/loan/list...");

        List<LoanDTO> response = loanService.requestLoanApi();
        return ResponseEntity.ok(response);
    }

}
