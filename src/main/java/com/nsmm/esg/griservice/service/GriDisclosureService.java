package com.nsmm.esg.griservice.service;

import com.nsmm.esg.griservice.dto.GriDisclosureRequest;
import com.nsmm.esg.griservice.dto.GriDisclosureResponse;
import com.nsmm.esg.griservice.entity.GriDisclosure;
import com.nsmm.esg.griservice.repository.GriDisclosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import com.nsmm.esg.griservice.exception.GriNotFoundException;
import com.nsmm.esg.griservice.exception.UnauthorizedGriAccessException;
import com.nsmm.esg.griservice.exception.DuplicateGriDisclosureException;

/**
 * GRI 공시 항목 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class GriDisclosureService {

    private final GriDisclosureRepository griDisclosureRepository;

    /**
     * [CREATE] GRI 항목 신규 등록
     *
     * 이 메서드는 특정 사용자가 동일한 GRI 코드를 중복 등록하지 못하도록
     * 먼저 해당 사용자와 GRI 코드로 기존 항목이 존재하는지 확인합니다.
     * 만약 이미 존재한다면, 중복 등록을 방지하기 위해 DuplicateGriDisclosureException을 발생시킵니다.
     *
     * @param memberId 인증된 사용자 ID
     * @param request GRI 공시 요청 DTO
     * @return 저장된 GRI 항목의 응답 DTO
     */
    public GriDisclosureResponse create(Long memberId, GriDisclosureRequest request) {
        // 중복 체크: 동일 사용자에 대해 동일 GRI 코드가 이미 존재하는지 확인
        griDisclosureRepository.findByMemberIdAndGriCode(memberId, request.getGriCode())
            .ifPresent(existing -> {
                // 중복 발견 시 예외 발생하여 클라이언트에 알림
                throw new DuplicateGriDisclosureException(request.getGriCode());
            });
        GriDisclosure saved = griDisclosureRepository.save(request.toEntity(memberId));
        return GriDisclosureResponse.fromEntity(saved);
    }

    /**
     * [UPDATE] GRI 항목 내용 수정
     *
     * 이 메서드는 GRI 항목을 수정할 때, 요청한 사용자가 해당 항목의 소유자인지 확인합니다.
     * findById로 항목을 조회한 후 filter를 사용하여 memberId가 일치하는지 검증합니다.
     * 만약 소유자가 아니라면 UnauthorizedGriAccessException 예외를 발생시켜 권한 없는 접근을 차단합니다.
     *
     * @param id 수정할 항목 ID
     * @param memberId 인증된 사용자 ID
     * @param content 수정할 내용
     * @return 수정된 항목의 응답 DTO
     */
    public GriDisclosureResponse update(Long id, Long memberId, String content) {
        // 항목 존재 여부 및 소유자 검증
        GriDisclosure disclosure = griDisclosureRepository.findById(id)
                .filter(d -> d.getMemberId().equals(memberId)) // 사용자 일치 여부 검증
                .orElseThrow(() -> new UnauthorizedGriAccessException("수정 권한이 없거나 GRI 항목이 없습니다."));
        // 수정 내용 반영
        disclosure.updateContent(content);
        return GriDisclosureResponse.fromEntity(disclosure);
    }

    /**
     * [READ] GRI 코드로 단일 항목 조회
     *
     * 특정 사용자가 등록한 GRI 항목 중, 지정한 GRI 코드에 해당하는 항목을 조회합니다.
     * 사용자별로 GRI 코드가 중복될 수 있으므로 memberId와 griCode를 모두 사용하여 정확한 항목을 찾습니다.
     * 만약 해당 항목이 존재하지 않으면 GriNotFoundException을 발생시켜 클라이언트에 알립니다.
     *
     * @param memberId 사용자 ID
     * @param griCode GRI 코드 (예: 401-1)
     * @return 해당 항목의 응답 DTO
     */
    public GriDisclosureResponse getByCode(Long memberId, String griCode) {
        GriDisclosure disclosure = griDisclosureRepository.findByMemberIdAndGriCode(memberId, griCode)
                .orElseThrow(() -> new GriNotFoundException("해당 코드의 GRI 항목이 없습니다."));
        return GriDisclosureResponse.fromEntity(disclosure);
    }

    /**
     * [READ] 사용자(memberId)가 등록한 전체 GRI 항목 조회
     *
     * 이 메서드는 특정 사용자가 등록한 모든 GRI 항목을 조회하여,
     * 각 엔티티를 GriDisclosureResponse DTO로 변환한 리스트를 반환합니다.
     * 이를 통해 클라이언트는 사용자별 GRI 공시 내역을 쉽게 확인할 수 있습니다.
     *
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
     *
     * 이 메서드는 삭제 요청자가 해당 GRI 항목의 소유자인지 확인합니다.
     * findById 후 filter로 소유자 검증을 수행하며, 소유자가 아니거나 항목이 없으면
     * UnauthorizedGriAccessException을 발생시켜 삭제 권한이 없음을 알립니다.
     * 권한 검증 후 정상적으로 항목을 삭제합니다.
     *
     * @param id 삭제할 항목 ID
     * @param memberId 인증된 사용자 ID
     */
    public void delete(Long id, Long memberId) {
        // 삭제 권한 확인: 소유자인지 검증
        GriDisclosure disclosure = griDisclosureRepository.findById(id)
                .filter(d -> d.getMemberId().equals(memberId)) // 권한 확인
                .orElseThrow(() -> new UnauthorizedGriAccessException("삭제 권한이 없거나 GRI 항목이 없습니다."));
        // 항목 삭제
        griDisclosureRepository.delete(disclosure);
    }
}
