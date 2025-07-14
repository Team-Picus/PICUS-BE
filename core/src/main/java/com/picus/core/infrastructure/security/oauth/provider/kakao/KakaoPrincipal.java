package com.picus.core.infrastructure.security.oauth.provider.kakao;

import com.picus.core.infrastructure.security.oauth.principal.AbstractOAuth2Principal;
import com.picus.core.user.domain.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;

public class KakaoPrincipal extends AbstractOAuth2Principal {

    public KakaoPrincipal(User user, KakaoProfile kakaoProfile) {
        super(user.getAuth().getProvider(),
                user.getAuth().getProviderId(),
                kakaoProfile.email(),
                kakaoProfile.tel(),
                kakaoProfile.name(),
                kakaoProfile.attributes(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
