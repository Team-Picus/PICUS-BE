package com.picus.core.user.application.port.in.mapper;

import com.picus.core.user.application.port.in.command.CompleteProfileCommand;
import com.picus.core.user.application.port.in.command.InitSocialUserCommand;
import com.picus.core.user.domain.model.Auth;
import com.picus.core.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserProfileCommandMapper {

    public User toDomainModel(InitSocialUserCommand command) {
        return User
                .builder()
                .auth(Auth
                        .builder()
                        .providerId(command.getProviderId())
                        .provider(command.getProvider())
                        .build())
                .email(command.getEmail())
                .name(command.getName())
                .tel(command.getTel())
                .build();
    }

    public User toDomainModel(CompleteProfileCommand command) {
        return User
                .builder()
                .userNo(command.getUserNo())
                .nickname(command.getNickname())
                .tel(command.getTel())
                .email(command.getEmail())
                .build();
    }
}
