package com.picus.core.infrastructure.security.oauth.principal;

import com.picus.core.user.domain.model.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class AbstractOAuth2Principal implements SocialPrincipal {

    protected final Provider provider;
    protected final String providerId;
    protected final String email;
    protected final String tel;
    protected final String name;
    protected final Map<String, Object> attributes;
    protected final Collection<? extends GrantedAuthority> authorities;

    // OAuth2User 구현
    @Override public Map<String, Object> getAttributes() { return attributes; }
    @Override public String getName() { return name; }
}
