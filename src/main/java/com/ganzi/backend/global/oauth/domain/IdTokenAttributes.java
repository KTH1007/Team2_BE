package com.ganzi.backend.global.oauth.domain;

import com.ganzi.backend.global.oauth.domain.info.KakaoUserInfo;
import com.ganzi.backend.global.oauth.domain.info.UserInfo;
import com.ganzi.backend.user.domain.RoleType;
import com.ganzi.backend.user.domain.User;
import java.util.Map;
import lombok.Getter;

@Getter
public class IdTokenAttributes {

    private final UserInfo userInfo;
    private final SocialProvider socialProvider;

    public IdTokenAttributes(Map<String, Object> attributes, SocialProvider socialProvider) {
        this.socialProvider = socialProvider;
        if (socialProvider == SocialProvider.KAKAO) {
            this.userInfo = new KakaoUserInfo(attributes);
        } else {
            this.userInfo = null;
        }
    }

    public User toUser() {
        return User.builder()
                .oauthId(userInfo.getId())
                .nickname(userInfo.getNickname())
                .profileImageUrl(userInfo.getProfileImageUrl())
                .email(userInfo.getEmail())
                .socialProvider(socialProvider)
                .roleType(RoleType.USER)
                .build();
    }
}
