package com.ganzi.backend.recommendation.infrastructure.dto;

import lombok.Builder;

@Builder
public record RecommendationSummaryResponse(
        String data1,
        String data2
) {}
