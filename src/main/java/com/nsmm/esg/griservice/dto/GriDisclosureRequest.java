package com.nsmm.esg.griservice.dto;

import com.nsmm.esg.griservice.entity.GriDisclosure;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class GriDisclosureRequest {
    private final String griCode;
    private final String indicator;
    private final String category;
    private final String content;

    public GriDisclosure toEntity(Long memberId) {
        return GriDisclosure.builder()
                .memberId(memberId)
                .griCode(griCode)
                .indicator(indicator)
                .category(category)
                .content(content)
                .build();
    }
}
