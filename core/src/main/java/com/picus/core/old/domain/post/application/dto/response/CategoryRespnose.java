package com.picus.core.old.domain.post.application.dto.response;

public record CategoryRespnose(
        String name,  // 예: enum의 이름 또는 displayName
        String type   // CategoryType의 이름
) {}