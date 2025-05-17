package com.nsmm.esg.griservice.controller;

import com.nsmm.esg.griservice.dto.GriProgressResponse;
import com.nsmm.esg.griservice.service.GriProgressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/gri")
public class GriProgressController {

    private final GriProgressService griProgressService;

    @GetMapping("/progress")
    public ResponseEntity<GriProgressResponse> getProgress(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return ResponseEntity.ok(griProgressService.getProgress(memberId));
    }

    private Long extractMemberId(HttpServletRequest request) {
        String header = request.getHeader("X-MEMBER-ID");
        if (header == null || header.isBlank()) {
            System.out.println("⚠️ X-MEMBER-ID 누락 → 기본값 1L 사용");
            return 1L;
        }
        return Long.parseLong(header);
    }
}