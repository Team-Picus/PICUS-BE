package com.picus.core.infrastructure.security.oauth.provider.kakao;

import com.picus.core.infrastructure.security.oauth.principal.AbstractOAuth2Principal;
import com.picus.core.user.domain.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;

public class KakaoPrincipal extends AbstractOAuth2Principal {

    public KakaoPrincipal(User user, Map<String,Object> attrs) {

        super(user.getAuth().getProvider(),
                user.getAuth().getProviderId(),
                user.getEmail(),
                extractNickname(attrs),
                attrs,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    private static String extractNickname(Map<String, Object> attrs) {
        Map<String, Object> account = (Map<String, Object>) attrs.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return (String) profile.get("nickname");
    }

}
