package com.ganzi.backend.global.oauth.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "Access Token")
        String accessToken,

        @Schema(description = "Refresh Token")
        String refreshToken,

        @Schema(description = "사용자 ID")
        Long userId,

        @Schema(description = "닉네임")
        String nickname
) {}
