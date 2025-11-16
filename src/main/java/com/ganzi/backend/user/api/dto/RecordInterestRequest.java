package com.ganzi.backend.user.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordInterestRequest (
        @NotBlank String desertionNo,
        Integer dwellTimeSeconds,
        boolean liked
){}
