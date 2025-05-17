package com.nsmm.esg.griservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GriProgressResponse {
    private final int totalCount;
    private final int completedCount;
    private final int incompleteCount;
    private final int completedRate;
}