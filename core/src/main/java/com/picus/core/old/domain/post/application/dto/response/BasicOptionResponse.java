package com.picus.core.old.domain.post.application.dto.response;

import java.util.List;

// BasicOption 정보를 담는 DTO
public record BasicOptionResponse(
        Integer basicPrice,
        List<AdditionalOptionResponse> additionalOptions
) {}