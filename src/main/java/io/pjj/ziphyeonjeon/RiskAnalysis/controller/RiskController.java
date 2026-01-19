package io.pjj.ziphyeonjeon.RiskAnalysis.controller;

import io.pjj.ziphyeonjeon.RiskAnalysis.domain.Risk;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskRequestDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskResponseDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.service.RiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    // DisasterApiCache DB 동기화
    @GetMapping("/refresh-disaster")
    public ResponseEntity<String> refreshDisaster() {
        riskService.refreshDisasterApiCache();
        return ResponseEntity.ok("재난 데이터 동기화 프로세스가 완료되었습니다. 콘솔 로그를 확인하세요.");
    }

    // 재해 위험 정보 조회
    @GetMapping("/disaster/{address}")
    public ResponseEntity<RiskResponseDTO> searchDisaster(@PathVariable String address) {
        System.out.println("/api/risk/disaster/{address}: " + address);

        RiskResponseDTO<RiskResponseDTO.DisasterResponse> response = riskService.searchDisasterAddress(address);
        return ResponseEntity.ok(response);
    }

    // 건축물대장 정보 조회
    @GetMapping("/building/{address}")
    public ResponseEntity<RiskResponseDTO<RiskResponseDTO.BuildingResponse>> analyzeBuilding(@PathVariable String address) {
        System.out.println("/api/risk/building/{address}: " + address);

        RiskResponseDTO<RiskResponseDTO.BuildingResponse> response = riskService.analyzeBuilding(address);

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