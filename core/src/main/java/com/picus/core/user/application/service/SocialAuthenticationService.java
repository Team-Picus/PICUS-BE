package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.mapper.UserProfileCommandMapper;
import com.picus.core.user.application.port.in.SocialAuthenticationUseCase;
import com.picus.core.user.application.port.in.command.InitSocialUserCommand;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SocialAuthenticationService implements SocialAuthenticationUseCase {

    private final UserCommandPort userCommandPort;
    private final UserProfileCommandMapper userProfileCommandMapper;

    @Override
    public User authenticate(InitSocialUserCommand command) {
        User user = userProfileCommandMapper.toDomainModel(command);
        return userCommandPort.upsert(user);
    }

}
