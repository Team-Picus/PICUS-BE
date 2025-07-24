package com.picus.core.user.adapter.out.persistence.mapper;

import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.domain.model.ProfileImage;
import org.springframework.stereotype.Component;

@Component
public class ProfileImagePersistenceMapper {

    public ProfileImage toDomain(ProfileImageEntity profileImageEntity) {
        return ProfileImage.builder()
                .profileImageNo(profileImageEntity.getProfileImageNo())
                .key(profileImageEntity.getFile_key())
                .userNo(profileImageEntity.getUserNo())
                .build();
    }
}
