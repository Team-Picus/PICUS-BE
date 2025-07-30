package com.picus.core.user.application.port.in.result;

import lombok.Builder;

@Builder
public record ReissueTokenResult(
        String accessToken
) {
}
