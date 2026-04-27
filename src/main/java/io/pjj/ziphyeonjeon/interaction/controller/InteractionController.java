package io.pjj.ziphyeonjeon.interaction.controller;

import io.pjj.ziphyeonjeon.interaction.entity.Likes;
import io.pjj.ziphyeonjeon.interaction.entity.Records;
import io.pjj.ziphyeonjeon.interaction.service.InteractionService;
import io.pjj.ziphyeonjeon.interaction.util.JwtMockUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interaction")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;
    private final JwtMockUtil jwtMockUtil; // 임시 파서

    @Data
    public static class LikeRequest {
        private Long houseId;
        private String name; // 주택이나 상가 이름 (화면 표시용)
    }

    @Data
    public static class RecordRequest {
        private Long houseId;
    }

    /**
     * 주택 하트(찜) 클릭 시 호출
     */
    @PostMapping("/likes")
    public ResponseEntity<Boolean> toggleLike(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody LikeRequest req) {
        
        // TODO: 로그인 기능 통합 시 진짜 JWT 검증 로직으로 대체
        Long userId = jwtMockUtil.extractUserIdFromToken(token);
        
        boolean isLiked = interactionService.toggleLike(userId, req.getHouseId(), req.getName());
        return ResponseEntity.ok(isLiked);
    }

    /**
     * 내 관심 주택 목록 조회
     */
    @GetMapping("/likes/me")
    public ResponseEntity<List<Likes>> getMyLikes(
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        Long userId = jwtMockUtil.extractUserIdFromToken(token);
        List<Likes> likes = interactionService.getMyLikes(userId);
        return ResponseEntity.ok(likes);
    }

    /**
     * 주택 다가구/단지 등을 눌렀을 때 열람 기록 추가
     */
    @PostMapping("/records")
    public ResponseEntity<Records> addViewRecord(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody RecordRequest req) {
        
        Long userId = jwtMockUtil.extractUserIdFromToken(token);
        Records record = interactionService.addViewRecord(userId, req.getHouseId());
        return ResponseEntity.ok(record);
    }

    /**
     * 내가 최근 본 매물 목록 전체 조회
     */
    @GetMapping("/records/me")
    public ResponseEntity<List<Records>> getMyRecords(
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        Long userId = jwtMockUtil.extractUserIdFromToken(token);
        List<Records> records = interactionService.getMyRecords(userId);
        return ResponseEntity.ok(records);
    }
}
