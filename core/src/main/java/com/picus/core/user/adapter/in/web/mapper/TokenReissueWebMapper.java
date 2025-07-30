package com.picus.core.user.adapter.in.web.mapper;

import com.picus.core.user.adapter.in.web.data.response.TokenReissueResponse;
import com.picus.core.user.application.port.in.result.ReissueTokenResult;
import org.springframework.stereotype.Component;

@Component
public class TokenReissueWebMapper {

    public TokenReissueResponse toResponse(ReissueTokenResult result) {
        return TokenReissueResponse
                .builder()
                .accessToken(result.accessToken())
                .build();
    }
}
