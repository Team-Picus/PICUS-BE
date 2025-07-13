package com.picus.core.infrastructure.security.oauth.principal;

import com.picus.core.user.domain.model.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface SocialPrincipal extends OAuth2User {
    Provider getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
