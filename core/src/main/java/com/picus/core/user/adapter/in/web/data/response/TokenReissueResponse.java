package com.picus.core.user.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record TokenReissueResponse(
        String accessToken
) {}
