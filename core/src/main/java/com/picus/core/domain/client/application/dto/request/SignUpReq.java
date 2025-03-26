package com.picus.core.domain.client.application.dto.request;

import java.util.List;

public record SignUpReq (
        String nickname,
        Long profileImgId,
        List<String> preferredArea
) {}
