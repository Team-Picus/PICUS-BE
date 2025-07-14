package com.picus.core.infrastructure.security.oauth.provider.apple;

import com.picus.core.infrastructure.security.oauth.principal.AbstractOAuth2Principal;
import com.picus.core.user.domain.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Map;

public class ApplePrincipal extends AbstractOAuth2Principal implements OidcUser {

    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;

    public ApplePrincipal(User user, Map<String,Object> attrs, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(user.getAuth().getProvider(),
                user.getAuth().getProviderId(),
                user.getEmail(),
                null,
                user.getName(),
                attrs,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        this.idToken  = idToken;
        this.userInfo = userInfo;
    }

    // OidcUser 구현
    @Override public OidcIdToken getIdToken()  { return idToken;  }

    @Override
    public Map<String, Object> getClaims() {
        return idToken.getClaims();
    }

    @Override public OidcUserInfo getUserInfo(){ return userInfo; }
}
