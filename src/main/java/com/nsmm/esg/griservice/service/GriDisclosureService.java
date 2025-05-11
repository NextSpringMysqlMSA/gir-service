package com.nsmm.esg.griservice.service;

import com.nsmm.esg.griservice.dto.GriDisclosureRequest;
import com.nsmm.esg.griservice.dto.GriDisclosureResponse;
import com.nsmm.esg.griservice.entity.GriDisclosure;
import com.nsmm.esg.griservice.repository.GriDisclosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GRI 공시 항목 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class GriDisclosureService {

    private final GriDisclosureRepository griDisclosureRepository;

    /**
     * [CREATE] GRI 항목 신규 등록
     * @param memberId 인증된 사용자 ID
     * @param request GRI 공시 요청 DTO
     * @return 저장된 GRI 항목의 응답 DTO
     */
    public GriDisclosureResponse create(Long memberId, GriDisclosureRequest request) {
        GriDisclosure saved = griDisclosureRepository.save(request.toEntity(memberId));
        return GriDisclosureResponse.fromEntity(saved);
    }

    /**
     * [UPDATE] GRI 항목 내용 수정
     * - 해당 memberId의 사용자만 수정 가능
     * @param id 수정할 항목 ID
     * @param memberId 인증된 사용자 ID
     * @param content 수정할 내용
     * @return 수정된 항목의 응답 DTO
     */
    public GriDisclosureResponse update(Long id, Long memberId, String content) {
        GriDisclosure disclosure = griDisclosureRepository.findById(id)
                .filter(d -> d.getMemberId().equals(memberId)) // 사용자 일치 여부 검증
                .orElseThrow(() -> new IllegalArgumentException("수정 권한이 없거나 GRI 항목이 없습니다."));
        disclosure.updateContent(content);
        return GriDisclosureResponse.fromEntity(disclosure);
    }

    /**
     * [READ] GRI 코드로 단일 항목 조회
     * @param memberId 사용자 ID
     * @param griCode GRI 코드 (예: 401-1)
     * @return 해당 항목의 응답 DTO
     */
    public GriDisclosureResponse getByCode(Long memberId, String griCode) {
        GriDisclosure disclosure = griDisclosureRepository.findByMemberIdAndGriCode(memberId, griCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 코드의 GRI 항목이 없습니다."));
        return GriDisclosureResponse.fromEntity(disclosure);
    }

    /**
     * [READ] 사용자(memberId)가 등록한 전체 GRI 항목 조회
     * @param memberId 사용자 ID
     * @return GRI 응답 DTO 리스트
     */
    public List<GriDisclosureResponse> getAll(Long memberId) {
        return griDisclosureRepository.findAllByMemberId(memberId).stream()
                .map(GriDisclosureResponse::fromEntity)
                .toList();
    }

    /**
     * [DELETE] GRI 항목 삭제
     * - 사용자(memberId) 소유 항목만 삭제 가능
     * @param id 삭제할 항목 ID
     * @param memberId 인증된 사용자 ID
     */
    public void delete(Long id, Long memberId) {
        GriDisclosure disclosure = griDisclosureRepository.findById(id)
                .filter(d -> d.getMemberId().equals(memberId)) // 권한 확인
                .orElseThrow(() -> new IllegalArgumentException("삭제 권한이 없거나 GRI 항목이 없습니다."));
        griDisclosureRepository.delete(disclosure);
    }
}
