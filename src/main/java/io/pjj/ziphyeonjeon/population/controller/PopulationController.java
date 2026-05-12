package io.pjj.ziphyeonjeon.population.controller;

import io.pjj.ziphyeonjeon.population.service.PopulationService;
import io.pjj.ziphyeonjeon.global.service.AdminAddressService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.pjj.ziphyeonjeon.population.dto.PopulationResponse;

@RestController
@RequestMapping("/api/populations")
@RequiredArgsConstructor
public class PopulationController {

    private final PopulationService populationService;
    private final AdminAddressService adminAddressService;

    @Operation(summary = "유동인구 데이터 일괄 동기화 (로컬 CSV)", description = "로컬에 위치한 유동인구 CSV 파일들을 스캔하여 백그라운드에서 유동인구 데이터를 DB에 일괄 저장 및 동기화합니다.")
    @PostMapping("/sync-local")
    public ResponseEntity<String> syncLocalCsvFiles() {
        CompletableFuture.runAsync(() -> {
            try {
                populationService.syncLocalCsvFiles();
            } catch (Exception e) {
            }
        });
        return ResponseEntity.ok("백그라운드에서 동기화 작업이 시작되었습니다. 서버 로그를 확인해주세요.");
    }

    @Operation(summary = "특정 행정구역 유동인구 조회", description = "행정구역명(address) 또는 행정구역 코드(ADSTRD_CD)를 입력받아 유동인구 데이터를 반환합니다.")
    @GetMapping
    public ResponseEntity<List<PopulationResponse>> getPopulation(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "ADSTRD_CD", required = false) String adstrdCd,
            @RequestParam(value = "REFERENCE_DATE", required = false) String referenceDate) {

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

        return ResponseEntity.ok(populationService.getPopulationByAdstrdCd(targetAdstrdCd, referenceDate));
    }
}
