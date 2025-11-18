package com.ganzi.backend.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecordLikeRequest(
        @NotBlank String desertionNo,
        @NotNull Boolean liked
){ }