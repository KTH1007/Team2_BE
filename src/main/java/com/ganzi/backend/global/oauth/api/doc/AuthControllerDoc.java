package com.ganzi.backend.global.oauth.api.doc;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.global.oauth.api.dto.response.LoginResponse;
import com.ganzi.backend.global.oauth.api.dto.response.UserInfoResponse;
import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "로그인", description = "로그인 API")
public interface AuthControllerDoc {

    @Operation(
            summary = "카카오 소셜 로그인",
            description = "카카오 ID Token으로 로그인하여 JWT 토큰을 발급받습니다"
    )
    ResponseEntity<ApiResponse<LoginResponse>> login(
            @Parameter(description = "카카오 ID Token", required = true)
            @RequestHeader("id_token") String idToken
    );

    @Operation(
            summary = "사용자 정보 조회",
            description = "인증된 사용자의 정보를 조회합니다"
    )
    ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(
            summary = "Access/Refresh 토큰 재발급",
            description = "Refresh Token을 사용하여 새로운 Access/Refresh 토큰 쌍을 발급받습니다. DB의 Refresh Token은 새로 발급된 토큰으로 업데이트됩니다."
    )
    ResponseEntity<ApiResponse<LoginResponse>> reissueTokens(
            @Parameter(description = "Access Token (Bearer 포함)", required = true)
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "Refresh Token", required = true)
            @RequestHeader("RefreshToken") String refreshToken
    );

    @Operation(
            summary = "로그아웃",
            description = "DB의 Refresh Token을 null로 변경하여 무효화합니다"
    )
    ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(description = "Access Token (Bearer 포함)", required = true)
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "Refresh Token", required = true)
            @RequestHeader("RefreshToken") String refreshToken
    );
}
