package com.picus.core.domain.post.application.dto.response;

import java.util.List;

// BasicOption 정보를 담는 DTO
public record BasicOptionDto(
        Integer basicPrice,
        List<AdditionalOptionDto> additionalOptions
) {}