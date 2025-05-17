package com.nsmm.esg.griservice.service;

import com.nsmm.esg.griservice.dto.GriProgressResponse;
import com.nsmm.esg.griservice.entity.GriDisclosure;
import com.nsmm.esg.griservice.repository.GriDisclosureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GriProgressService {

    private final GriDisclosureRepository griDisclosureRepository;

    // GRI 표준 항목 총 개수 (고정)
    private static final int TOTAL_GRI_COUNT = 119;

    public GriProgressResponse getProgress(Long memberId) {
        List<GriDisclosure> disclosures = griDisclosureRepository.findAllByMemberId(memberId);

        int completed = (int) disclosures.stream()
                .filter(d -> d.getContent() != null && !d.getContent().trim().isEmpty())
                .count();

        int incomplete = TOTAL_GRI_COUNT - completed;
        int completedRate = (int) ((completed * 100.0) / TOTAL_GRI_COUNT);

        // 로그 출력
        log.info("📊 [GRI 진행 현황] memberId={} 총합={}, 완료={}, 미완료={}, 완료율={}%",
                memberId, TOTAL_GRI_COUNT, completed, incomplete, completedRate);

        return GriProgressResponse.builder()
                .totalCount(TOTAL_GRI_COUNT)
                .completedCount(completed)
                .incompleteCount(incomplete)
                .completedRate(completedRate)
                .build();
    }
}