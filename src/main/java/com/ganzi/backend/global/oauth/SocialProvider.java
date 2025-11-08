package com.ganzi.backend.global.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialProvider {
    KAKAO("kakao");

    private final String registrationId;
}
