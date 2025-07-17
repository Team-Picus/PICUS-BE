package com.picus.core.user.application.port.out.response;

public record UserWithProfileImageDto(
        String nickname,
        String profileImageFileKey,
        String expertNo
) {
}
