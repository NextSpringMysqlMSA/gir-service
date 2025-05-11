package com.nsmm.esg.griservice.dto;

import com.nsmm.esg.griservice.entity.GriDisclosure;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GriDisclosureResponse {
    private final Long id;
    private final String griCode;
    private final String indicator;
    private final String category;
    private final String content;
    private final Long memberId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static GriDisclosureResponse fromEntity(GriDisclosure entity) {
        return GriDisclosureResponse.builder()
                .id(entity.getId())
                .griCode(entity.getGriCode())
                .indicator(entity.getIndicator())
                .category(entity.getCategory())
                .content(entity.getContent())
                .memberId(entity.getMemberId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
