package com.nsmm.esg.griservice.controller;

import com.nsmm.esg.griservice.dto.GriDisclosureRequest;
import com.nsmm.esg.griservice.dto.GriDisclosureResponse;
import com.nsmm.esg.griservice.service.GriDisclosureService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.nsmm.esg.griservice.dto.ErrorResponse;
import com.nsmm.esg.griservice.exception.DuplicateGriDisclosureException;

import java.util.List;

/**
 * GRI 공시 항목에 대한 CRUD API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gri")
public class GriDisclosureController {

    private final GriDisclosureService griDisclosureService;

    /**
     * 인증된 사용자 ID를 요청 헤더에서 추출
     * - 헤더: X-MEMBER-ID
     * - 없거나 비어있으면 기본값 1L 반환
     */
    private Long extractMemberId(HttpServletRequest request) {
        String memberIdHeader = request.getHeader("X-MEMBER-ID");
        if (memberIdHeader == null || memberIdHeader.isBlank()) {
            System.out.println("⚠️ X-MEMBER-ID 누락 → 기본값 1L 사용");
            return 1L;
        }
        return Long.parseLong(memberIdHeader);
    }

    /**
     * [GET] 전체 GRI 공시 항목 목록 조회
     * - 로그인된 사용자(memberId)의 GRI 공시 항목 전체를 조회
     * - X-MEMBER-ID 헤더에서 사용자 ID를 추출
     * - 해당 ID로 등록된 모든 GRI 항목을 조회하여 List 형태로 반환
     * - 비로그인 테스트 시 기본값 1L로 처리됨
     */
    @GetMapping
    public ResponseEntity<List<GriDisclosureResponse>> getAll(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        List<GriDisclosureResponse> responses = griDisclosureService.getAll(memberId);
        return ResponseEntity.ok(responses);
    }

    /**
     * [GET] GRI 공시 항목 단건 조회
     * - PathVariable로 전달된 griCode와 사용자 ID를 기반으로 해당 GRI 항목을 조회
     * - X-MEMBER-ID 헤더에서 사용자 ID를 추출
     * - 본인 소유의 GRI 항목만 조회 가능하며, 없을 경우 예외 발생
     */
    @GetMapping("/{griCode}")
    public ResponseEntity<GriDisclosureResponse> getByCode(@PathVariable String griCode, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        GriDisclosureResponse response = griDisclosureService.getByCode(memberId, griCode);
        return ResponseEntity.ok(response);
    }

    /**
     * [POST] GRI 공시 항목 등록
     * - 요청 바디(GriDisclosureRequest)에 담긴 indicator, griCode, category, content를 저장
     * - X-MEMBER-ID 헤더에서 사용자 ID를 추출하여 등록 주체로 사용
     * - 이미 동일 griCode가 존재할 경우 중복 예외 발생
     */
    @PostMapping
    public ResponseEntity<GriDisclosureResponse> create(@RequestBody GriDisclosureRequest request, HttpServletRequest httpRequest) {
        Long memberId = extractMemberId(httpRequest);
        GriDisclosureResponse response = griDisclosureService.create(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * [PUT] GRI 공시 항목 수정
     * - 요청 바디(GriDisclosureRequest)의 content를 수정 대상 ID에 반영
     * - 해당 항목의 작성자가 본인인지 검증 후 수정 진행
     * - ID 또는 권한이 없을 경우 예외 발생
     */
    @PutMapping("/{id}")
    public ResponseEntity<GriDisclosureResponse> update(@PathVariable Long id, @RequestBody GriDisclosureRequest request, HttpServletRequest httpRequest) {
        Long memberId = extractMemberId(httpRequest);
        GriDisclosureResponse response = griDisclosureService.update(id, memberId, request.getContent());
        return ResponseEntity.ok(response);
    }

    /**
     * [DELETE] GRI 공시 항목 삭제
     * - URL의 ID에 해당하는 항목을 삭제
     * - 요청자의 사용자 ID를 확인하여 본인 항목만 삭제 가능
     * - ID 또는 권한이 없을 경우 예외 발생
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        griDisclosureService.delete(id, memberId);
        return ResponseEntity.ok("🗑️ GRI 항목 삭제 완료. ID = " + id);
    }
}
