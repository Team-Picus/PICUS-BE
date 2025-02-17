package com.picus.core.global.oauth.service;

import com.picus.core.domain.user.entity.User;
import com.picus.core.domain.user.entity.UserType;
import com.picus.core.domain.user.entity.profile.Gender;
import com.picus.core.domain.user.entity.profile.Profile;
import com.picus.core.domain.user.repository.UserRepository;
import com.picus.core.global.oauth.entity.Provider;
import com.picus.core.global.oauth.entity.UserPrincipal;
import com.picus.core.global.oauth.info.OAuth2UserInfo;
import com.picus.core.global.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService 호출");
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * OAuth2User 정보를 가지고 유저 생성 및 업데이트
     * @param userRequest OAuth2UserRequest
     * @param user OAuth2User
     * @return UserPrincipal
     */
    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        Provider providerType = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Optional<User> userOptional = userRepository.findByProviderIdAndProvider(userInfo.getId(), providerType);
        User savedUser = userOptional.orElse(null);

        if (userOptional.isEmpty()) {
            this.createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    /**
     * 신규 유저 생성
     * @param userInfo 유저 정보
     * @param providerType Provider 타입
     * @return 생성된 유저
     */
    private User createUser(OAuth2UserInfo userInfo, Provider providerType) {
        Profile profile = new Profile(userInfo.getName(),
                userInfo.getName(),
                "01010101010", // TODO: OAuth2UserInfo 에 추가되어야한다.
                userInfo.getEmail(),
                Gender.MALE, // TODO: OAuth2UserInfo 에 추가되어야한다.
                userInfo.getImageUrl());

        User user = User.builder()
                .profile(profile)
                .provider(providerType)
                .providerId(userInfo.getId())
                .userType(UserType.CLIENT)
                .build();

        return userRepository.saveAndFlush(user);
    }

}
