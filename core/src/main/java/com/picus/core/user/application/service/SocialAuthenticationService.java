package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.SocialAuthenticationUseCase;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SocialAuthenticationService implements SocialAuthenticationUseCase {

    private final UserCommandPort userCommandPort;

    @Override
    public User authenticate(String providerId, Provider provider, String email, String name, String tel) {
        return userCommandPort.upsert(providerId, provider, email, name, tel);
    }

}
