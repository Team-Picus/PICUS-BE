package com.picus.core.domain.client.application.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record SignUpReq (
        @NotEmpty String nickname,
        Long profileImgId,
        @NotEmpty Set<String> preferredArea
) {}
