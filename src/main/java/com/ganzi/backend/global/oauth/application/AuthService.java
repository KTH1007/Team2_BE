package com.ganzi.backend.global.oauth.application;

import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import com.ganzi.backend.global.oauth.api.dto.response.LoginResponse;
import com.ganzi.backend.global.oauth.api.dto.response.UserInfoResponse;
import com.ganzi.backend.global.security.jwt.JwtService;
import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import com.ganzi.backend.user.User;
import com.ganzi.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final IdTokenService idTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public LoginResponse login(String idToken) {
        CustomUserDetails userDetails = idTokenService.loadUserByIdToken(idToken);
        User user = userDetails.getUser();

        String accessToken = jwtService.createAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.updateRefreshToken(user.getEmail(), refreshToken);

        return new LoginResponse(accessToken, refreshToken, user.getId(), user.getNickname());
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return UserInfoResponse.from(user);
    }

    @Transactional
    public LoginResponse reissueTokens(String oldAccessToken, String oldRefreshToken) {
        if (!jwtService.isTokenValid(oldRefreshToken)) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }

        User user = userRepository.findByRefreshToken(oldRefreshToken)
                .orElseThrow(() -> new GeneralException(ErrorStatus.EXPIRED_TOKEN));

        String newAccessToken = jwtService.createAccessToken(user.getEmail(), user.getId());
        String newRefreshToken = jwtService.createRefreshToken();

        jwtService.updateRefreshToken(user.getEmail(), newRefreshToken);

        return new LoginResponse(newAccessToken, newRefreshToken, user.getId(), user.getNickname());
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        String email = jwtService.verifyTokenAndGetEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        jwtService.updateRefreshToken(email, null);
    }
}
