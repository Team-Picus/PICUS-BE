package com.picus.core.user.application.port.out.response;

import lombok.Builder;

@Builder
public record UserWithProfileImageDto(
        String nickname,
        String profileImageFileKey,
        String expertNo
) {
}
