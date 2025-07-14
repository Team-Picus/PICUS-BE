package com.picus.core.infrastructure.security.oauth.factory;

import com.picus.core.infrastructure.security.oauth.principal.SocialPrincipal;
import com.picus.core.infrastructure.security.oauth.provider.kakao.KakaoAttributeConverter;
import com.picus.core.infrastructure.security.oauth.provider.kakao.KakaoPrincipal;
import com.picus.core.infrastructure.security.oauth.provider.kakao.KakaoProfile;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.SocialAuthenticationUseCase;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import static com.picus.core.shared.exception.code.status.AuthErrorStatus.INVALID_PROVIDER;

@Component
@RequiredArgsConstructor
public class SocialPrincipalFactory {

    private final KakaoAttributeConverter kakaoConv;
    private final SocialAuthenticationUseCase socialAuthenticationUseCase;

    public SocialPrincipal create(String registrationId, OAuth2User oAuth2User) {
        return switch (registrationId) {
            case "kakao" -> fromKakao(kakaoConv.convert(oAuth2User.getAttributes()));
//            case "apple" -> fromApple(appleConv.convert((OidcUser) oAuth2User));
            default -> throw new RestApiException(INVALID_PROVIDER);
        };
    }

//    public

    private SocialPrincipal fromKakao(KakaoProfile kakaoProfile) {
        User user = socialAuthenticationUseCase.authenticate(kakaoProfile.providerId(), Provider.KAKAO, kakaoProfile.email(), kakaoProfile.name(), kakaoProfile.tel());
        return new KakaoPrincipal(user, kakaoProfile);
    }
}