package com.ganzi.backend.global.security.jwt;

import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import com.ganzi.backend.user.domain.User;
import com.ganzi.backend.user.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";
    private static final String USERID_CLAIM = "userId";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private final UserRepository userRepository;

    public String createAccessToken(String email, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationPeriod);

        return Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .claim(EMAIL_CLAIM, email)
                .claim(USERID_CLAIM, userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationPeriod);

        return Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            Claims claims = parseClaims(accessToken);
            return Optional.ofNullable(claims.get(EMAIL_CLAIM, String.class));
        } catch (Exception e) {
            log.error("Access Token에서 Email 추출 실패", e);
            return Optional.empty();
        }
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public Optional<Long> extractUserId(String accessToken) {
        try {
            Claims claims = parseClaims(accessToken);
            return Optional.ofNullable(claims.get(USERID_CLAIM, Long.class));
        } catch (Exception e) {
            log.error("Access Token에서 UserId 추출 실패", e);
            return Optional.empty();
        }
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, BEARER + accessToken);
        log.info("재발급된 Access Token: {}", accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        writeAccessTokenHeader(response, accessToken);
        writeRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    @Transactional
    public void updateRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        user.updateRefreshToken(refreshToken);
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            return false;
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰입니다.");
            return false;
        } catch (io.jsonwebtoken.security.SignatureException e) {  // ✅ 패키지 명시
            log.error("잘못된 JWT 서명입니다.");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 비어있습니다.");
            return false;
        }
    }

    public String verifyTokenAndGetEmail(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.get(EMAIL_CLAIM, String.class);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰입니다. {}", e.getMessage());
            throw new GeneralException(ErrorStatus.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException |
                 io.jsonwebtoken.security.SignatureException e) {
            log.warn("유효하지 않은 토큰입니다. {}", e.getMessage());
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 비어있습니다.");
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }
    }

    public Long verifyTokenAndGetUserId(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.get(USERID_CLAIM, Long.class);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰입니다. {}", e.getMessage());
            throw new GeneralException(ErrorStatus.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException |
                 io.jsonwebtoken.security.SignatureException e) {
            log.warn("유효하지 않은 토큰입니다. {}", e.getMessage());
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 비어있습니다.");
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void writeAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, BEARER + accessToken);
    }

    private void writeRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, BEARER + refreshToken);
    }
}
