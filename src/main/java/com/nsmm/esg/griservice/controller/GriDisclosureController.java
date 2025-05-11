package com.nsmm.esg.griservice.controller;

import com.nsmm.esg.griservice.dto.GriDisclosureRequest;
import com.nsmm.esg.griservice.dto.GriDisclosureResponse;
import com.nsmm.esg.griservice.service.GriDisclosureService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * - 로그인된 사용자(memberId)의 데이터만 조회
     */
    @GetMapping
    public List<GriDisclosureResponse> getAll(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return griDisclosureService.getAll(memberId);
    }

    /**
     * [GET] 특정 GRI 코드(griCode)로 공시 항목 단건 조회
     * - memberId + griCode로 식별
     */
    @GetMapping("/{griCode}")
    public GriDisclosureResponse getByCode(@PathVariable String griCode, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return griDisclosureService.getByCode(memberId, griCode);
    }

    /**
     * [POST] GRI 항목 새로 등록
     * - 요청 바디에 indicator, griCode, category, content 포함
     * - memberId는 헤더에서 추출
     */
    @PostMapping
    public GriDisclosureResponse create(@RequestBody GriDisclosureRequest request, HttpServletRequest httpRequest) {
        Long memberId = extractMemberId(httpRequest);
        return griDisclosureService.create(memberId, request);
    }

    /**
     * [PUT] GRI 항목 내용(content) 수정
     * - 본인(memberId)이 작성한 항목만 수정 가능
     * - 수정할 항목 ID는 URL에서 전달
     */
    @PutMapping("/{id}")
    public GriDisclosureResponse update(@PathVariable Long id, @RequestBody GriDisclosureRequest request, HttpServletRequest httpRequest) {
        Long memberId = extractMemberId(httpRequest);
        return griDisclosureService.update(id, memberId, request.getContent());
    }

    /**
     * [DELETE] GRI 항목 삭제
     * - 본인(memberId)이 작성한 항목만 삭제 가능
     * - 삭제할 항목 ID는 URL에서 전달
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        griDisclosureService.delete(id, memberId);
        return "🗑️ GRI 항목 삭제 완료. ID = " + id;
    }
}
