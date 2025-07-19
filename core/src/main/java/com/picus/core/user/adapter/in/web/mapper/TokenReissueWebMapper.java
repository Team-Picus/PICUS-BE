package com.picus.core.user.adapter.in.web.mapper;

import com.picus.core.user.adapter.in.web.data.response.TokenReissueResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenReissueWebMapper {

    public TokenReissueResponse toDto(String accessToken) {
        return TokenReissueResponse
                .builder()
                .accessToken(accessToken)
                .build();
    }
}
