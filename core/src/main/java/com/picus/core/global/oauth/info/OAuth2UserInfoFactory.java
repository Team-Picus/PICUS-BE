package com.picus.core.global.oauth.info;

import com.picus.core.global.oauth.entity.Provider;
import com.picus.core.global.oauth.info.impl.FacebookOAuth2UserInfo;
import com.picus.core.global.oauth.info.impl.KakaoOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
        switch (provider) {
            case FACEBOOK: return new FacebookOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
