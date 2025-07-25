package com.picus.core.user.adapter.out.persistence.mapper;

import com.picus.core.user.adapter.out.persistence.entity.TermEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserTermEntity;
import org.springframework.stereotype.Component;

@Component
public class UserTermPersistenceMapper {

    public UserTermEntity toEntity(UserEntity userEntity, TermEntity termEntity, Boolean isAgreed) {
        return UserTermEntity.builder()
                .user(userEntity)
                .term(termEntity)
                .isAgreed(isAgreed)
                .build();
    }
}
