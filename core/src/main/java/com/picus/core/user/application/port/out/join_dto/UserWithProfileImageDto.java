package com.picus.core.user.application.port.out.join_dto;

import lombok.Builder;

@Builder
public record UserWithProfileImageDto(
        String nickname,
        String profileImageFileKey,
        String expertNo
) {
}
