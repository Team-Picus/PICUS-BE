package com.picus.core.domain.post.application.dto.response;

public record DistrictDto(
        String enumValue,   // District enum의 상수 이름
        String displayName  // District의 표시용 이름
) {
}

