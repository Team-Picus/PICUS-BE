package com.picus.core.domain.client.application.dto.request;

import java.util.List;
import java.util.Set;

public record SignUpReq (
        String nickname,
        Long profileImgId,
        Set<String> preferredArea
) {}
