package com.ganzi.backend.global.oauth.api.dto.response;

import com.ganzi.backend.user.RoleType;
import com.ganzi.backend.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 응답")
public record UserInfoResponse(
        @Schema(description = "이메일")
        String email,

        @Schema(description = "닉네임")
        String nickname,

        @Schema(description = "프로필 이미지 URL")
        String profileImageUrl,

        @Schema(description = "권한")
        RoleType roleType
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getRoleType()
        );
    }
}
