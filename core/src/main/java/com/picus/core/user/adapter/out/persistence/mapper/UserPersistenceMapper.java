package com.picus.core.user.adapter.out.persistence.mapper;

import com.picus.core.user.domain.model.Auth;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.User;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity mapToUserEntity(String providerId,
                                      Provider provider,
                                      String email,
                                      String name,
                                      String tel) {
        return UserEntity.builder()
                .providerId(providerId)
                .provider(provider)
                .email(email)
                .name(name)
                .tel(tel)
                .build();
    }

    public User mapToUser(UserEntity userEntity) {
        return User.builder()
                .userNo(userEntity.getUserNo())
                .auth(Auth.builder()
                        .providerId(userEntity.getProviderId())
                        .provider(userEntity.getProvider())
                        .build())
                .role(userEntity.getRole())
                .build();
    }
}
