package io.pjj.ziphyeonjeon.RiskAnalysis.controller;

import io.pjj.ziphyeonjeon.RiskAnalysis.domain.Risk;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskRequestDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskResponseDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.service.RiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    // DisasterApiCache DB 동기화
    @GetMapping("/refresh-disaster")
    public ResponseEntity<String> refreshDisaster() {
        riskService.refreshApiCache();
        return ResponseEntity.ok("데이터 동기화 프로세스가 완료되었습니다. 콘솔 로그를 확인하세요.");
    }

    // 종합 위험도 분석 - 부동산 주소
    @GetMapping("/{address}")
    public ResponseEntity<RiskResponseDTO> searchRiskAddress(@PathVariable String address) {
        System.out.println("/api/risk/{address}: " + address);

        RiskResponseDTO response = riskService.searchAddress(address);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/riskAnalyze")
    public ResponseEntity<Risk> analyze(@RequestBody RiskRequestDTO.DisasterRequest request) {
        // Api 호출하고 등급 산정하기

        // 테스트 데이터
        Risk savedRisk = riskService.scoreSave(1L, "주의");

        return ResponseEntity.ok(savedRisk);
    }
}