package com.ganzi.backend.global.oauth.application;

import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import com.ganzi.backend.global.oauth.domain.IdTokenAttributes;
import com.ganzi.backend.global.oauth.domain.SocialProvider;
import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import com.ganzi.backend.user.domain.User;
import com.ganzi.backend.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdTokenService {

    private static final String KAKAO_ISSUER = "https://kauth.kakao.com";
    private static final String CLAIM_SUB = "sub";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_NICKNAME = "nickname";
    private static final String CLAIM_PICTURE = "picture";

    private final UserRepository userRepository;

    public CustomUserDetails loadUserByIdToken(String idToken) {
        try {
            Claims claims = decodeIdToken(idToken);
            SocialProvider socialProvider = checkIssuer(claims.getIssuer());

            Map<String, Object> attributes = claimsToAttributes(claims, socialProvider);
            IdTokenAttributes idTokenAttributes = new IdTokenAttributes(attributes, socialProvider);

            User findUser = checkUser(idTokenAttributes);

            return new CustomUserDetails(findUser);
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            log.warn("ID 토큰 인증 오류: {}", e.getMessage());
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }
    }

    private Claims decodeIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new GeneralException(ErrorStatus.INVALID_TOKEN);
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> claimsMap = mapper.readValue(payload, Map.class);

            return Jwts.claims().add(claimsMap).build();
        } catch (Exception e) {
            log.error("ID 토큰 디코딩 실패", e);
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }
    }

    private SocialProvider checkIssuer(String issuer) {
        if (KAKAO_ISSUER.equals(issuer)) {
            return SocialProvider.KAKAO;
        }
        log.warn("지원하지 않는 소셜 로그인 제공자입니다. issuer: {}", issuer);
        throw new GeneralException(ErrorStatus.INVALID_TOKEN);
    }

    @Transactional
    public User checkUser(IdTokenAttributes idTokenAttributes) {
        String email = idTokenAttributes.getUserInfo().getEmail();
        User findUser = userRepository.findByEmail(email).orElse(null);

        if (findUser == null) {
            return createUser(idTokenAttributes);
        }

        return findUser;
    }

    private User createUser(IdTokenAttributes idTokenAttributes) {
        User createdUser = idTokenAttributes.toUser();
        return userRepository.save(createdUser);
    }

    private Map<String, Object> claimsToAttributes(Claims claims, SocialProvider socialProvider) {
        if (socialProvider == SocialProvider.KAKAO) {
            return Map.of(
                    CLAIM_SUB, claims.getSubject(),
                    CLAIM_EMAIL, claims.get(CLAIM_EMAIL, String.class),
                    CLAIM_NICKNAME, claims.get(CLAIM_NICKNAME, String.class),
                    CLAIM_PICTURE, claims.get(CLAIM_PICTURE, String.class)
            );
        }
        return null;
    }
}
