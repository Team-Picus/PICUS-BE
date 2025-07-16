package com.picus.core.user.adapter.out.persistence.mapper;

import com.picus.core.user.domain.model.Auth;
import com.picus.core.user.domain.model.User;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userNo(user.getUserNo())
                .name(user.getName())
                .nickname(user.getNickname())
                .tel(user.getTel())
                .role(user.getRole())
                .email(user.getEmail())
                .providerId(user.getAuth().getProviderId())
                .provider(user.getAuth().getProvider())
                .reservationHistoryCount(user.getReservationHistoryCount())
                .followCount(user.getFollowCount())
                .myMoodboardCount(user.getMyMoodboardCount())
                .expertNo(user.getExpertNo())
                .build();
    }

    public User toDomainModel(UserEntity userEntity) {
        return User.builder()
                .userNo(userEntity.getUserNo())
                .name(userEntity.getName())
                .role(userEntity.getRole())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .tel(userEntity.getTel())
                .reservationHistoryCount(userEntity.getReservationHistoryCount())
                .followCount(userEntity.getFollowCount())
                .myMoodboardCount(userEntity.getMyMoodboardCount())
                .expertNo(userEntity.getExpertNo())
                .auth(Auth.builder()
                        .providerId(userEntity.getProviderId())
                        .provider(userEntity.getProvider())
                        .build())
                .build();
    }


}
