package io.pjj.ziphyeonjeon.RiskAnalysis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskResponseDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.service.RiskAnalysisService;
import io.pjj.ziphyeonjeon.RiskAnalysis.service.RiskOcrService;

import java.io.IOException;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RiskController {

    private final RiskAnalysisService riskAnalysisService;
    private final RiskOcrService riskOcrService;

    public RiskController(RiskAnalysisService riskAnalysisService, RiskOcrService riskOcrService) {
        this.riskAnalysisService = riskAnalysisService;
        this.riskOcrService = riskOcrService;
    }

    // 재해 위험 정보 조회
    @GetMapping("/disaster/{address}")
    public ResponseEntity<RiskResponseDTO<RiskResponseDTO.DisasterResponse>> searchDisaster(@PathVariable String address) {
        System.out.println("/api/risk/disaster/{address}: " + address);

        RiskResponseDTO<RiskResponseDTO.DisasterResponse> response = riskAnalysisService.analyzeDisasterRisk(address);
        return ResponseEntity.ok(response);
    }

    // 건축물대장 정보 조회
    @GetMapping("/building/{address}")
    public ResponseEntity<RiskResponseDTO<RiskResponseDTO.BuildingResponse>> analyzeBuilding(@PathVariable String address) {
        System.out.println("/api/risk/building/{address}: " + address);

        RiskResponseDTO<RiskResponseDTO.BuildingResponse> response = riskAnalysisService.analyzeBuildingRisk(address);

        return ResponseEntity.ok(response);
    }

    // 등기부등본 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadRegistry(
            @RequestParam("address") String address,
            @RequestParam("requestId") String requestId,
            @RequestParam("file") MultipartFile file) {

        try {
            String savedFileName = riskOcrService.saveFile(address, requestId, file);

            return ResponseEntity.ok("/api/risk/upload/: " + savedFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("/upload 컨트롤러 오류 발생: " + e.getMessage());
        }
    }

    // OCR 전송
    @PostMapping("/ocr")
    public ResponseEntity<?> requestOcr(@RequestParam String message,
                                        @RequestParam MultipartFile file) {
        try {
            RiskResponseDTO<RiskResponseDTO.RecordOfTitleResponse> result = riskAnalysisService.analyzeRecordOfTitleRisk(message, file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("/ocr 컨트롤러 오류 발생: " + e.getMessage());
        }
    }
}