package com.picus.core.infrastructure.security.oauth.service;

import com.picus.core.infrastructure.security.oauth.factory.SocialPrincipalFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialPrincipalFactory factory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User rawUser = super.loadUser(req);
        return factory.create(req.getClientRegistration().getRegistrationId(), rawUser);
    }
}
