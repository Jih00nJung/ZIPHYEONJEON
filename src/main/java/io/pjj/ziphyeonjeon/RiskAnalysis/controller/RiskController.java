package io.pjj.ziphyeonjeon.RiskAnalysis.controller;

import io.pjj.ziphyeonjeon.RiskAnalysis.dto.OcrDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.dto.RiskResponseDTO;
import io.pjj.ziphyeonjeon.RiskAnalysis.service.RiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    // 재해 위험 정보 조회
    @GetMapping("/disaster/{address}")
    public ResponseEntity<RiskResponseDTO<RiskResponseDTO.DisasterResponse>> searchDisaster(@PathVariable String address) {
        System.out.println("/api/risk/disaster/{address}: " + address);

        RiskResponseDTO<RiskResponseDTO.DisasterResponse> response = riskService.sendDisasterApi(address);
        return ResponseEntity.ok(response);
    }

    // 건축물대장 정보 조회
    @GetMapping("/building/{address}")
    public ResponseEntity<RiskResponseDTO<RiskResponseDTO.BuildingResponse>> analyzeBuilding(@PathVariable String address) {
        System.out.println("/api/risk/building/{address}: " + address);

        RiskResponseDTO<RiskResponseDTO.BuildingResponse> response = riskService.sendBuildingApi(address);

        return ResponseEntity.ok(response);
    }

    // 등기부등본 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadRegistry(
            @RequestParam("address") String address,
            @RequestParam("requestId") String requestId,
            @RequestParam("file") MultipartFile file) {

        try {
            String savedFileName = riskService.saveFile(address, requestId, file);

            return ResponseEntity.ok("/api/risk/upload/: " + savedFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("/upload 컨트롤러 오류 발생: " + e.getMessage());
        }
    }

    // OCR 전송
    @PostMapping("/ocr")
    public ResponseEntity<?> requestOcr(@RequestParam("message") String message,
                                           @RequestParam("file") MultipartFile file) {
        try {
            RiskResponseDTO<RiskResponseDTO.RecordOfTitleResponse> result = riskService.analyzeRecordOfTitleRisk(message, file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("/ocr 컨트롤러 오류 발생: " + e.getMessage());
        }
    }
}