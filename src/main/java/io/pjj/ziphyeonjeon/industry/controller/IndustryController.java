package io.pjj.ziphyeonjeon.industry.controller;

import io.pjj.ziphyeonjeon.industry.service.IndustryService;
import io.pjj.ziphyeonjeon.global.service.AdminAddressService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.*;
import io.pjj.ziphyeonjeon.industry.dto.IndustryResponse;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/industry")
@RequiredArgsConstructor
@Validated
public class IndustryController {

    private final IndustryService industryService;
    private final AdminAddressService adminAddressService;

    @Operation(summary = "상권 점포 데이터 일괄 동기화 (로컬 CSV)", description = "로컬에 저장된 서울시 상권분석서비스(점포-행정동).csv 파일을 읽어들여 상권 점포 데이터를 DB에 일괄 저장 및 동기화합니다.")
    @PostMapping("/sync-local")
    public ResponseEntity<String> syncLocalCsv() {
        industryService.syncLocalCsv();
        return ResponseEntity.ok("CSV sync completed successfully.");
    }

    @Operation(summary = "특정 행정구역 상권 데이터 조회", description = "행정구역명(address) 또는 행정동 코드(ADSTRD_CD) 8자리로 데이터를 조회합니다.")
    @GetMapping("/code")
    public ResponseEntity<List<IndustryResponse>> getIndustryByAdstrdCd(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "ADSTRD_CD", required = false) String adstrdCd) {
        
        String targetAdstrdCd = adstrdCd;

        if (address != null && !address.isBlank()) {
            targetAdstrdCd = adminAddressService.getAdstrdCd(address);
            if (targetAdstrdCd == null) {
                throw new IllegalArgumentException("해당 행정구역명을 찾을 수 없습니다: " + address);
            }
        }

        if (targetAdstrdCd == null || !targetAdstrdCd.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("올바른 행정구역명(address) 또는 8자리 숫자 행정구역코드(ADSTRD_CD)를 입력해야 합니다.");
        }

        return ResponseEntity.ok(industryService.getIndustryByAdstrdCd(targetAdstrdCd));
    }

    @Operation(summary = "특정 서비스 업종명 상권 데이터 조회", description = "서비스 업종명(SVC_INDUTY_NM)으로 데이터를 조회합니다.")
    @GetMapping("/name/{SVC_INDUTY_NM}")
    public ResponseEntity<List<IndustryResponse>> getIndustryBySvcIndutyNm(
            @PathVariable("SVC_INDUTY_NM") @NotBlank(message = "SVC_INDUTY_NM은 비어있을 수 없습니다.") String svcIndutyNm) {
        return ResponseEntity.ok(industryService.getIndustryBySvcIndutyNm(svcIndutyNm));
    }
}
