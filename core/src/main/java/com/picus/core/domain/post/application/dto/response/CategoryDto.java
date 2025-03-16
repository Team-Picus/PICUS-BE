package com.picus.core.domain.post.application.dto.response;

public record CategoryDto(
        String name,  // 예: enum의 이름 또는 displayName
        String type   // CategoryType의 이름
) {}