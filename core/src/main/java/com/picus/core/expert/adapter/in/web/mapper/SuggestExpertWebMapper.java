package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SuggestExpertResponse;
import com.picus.core.expert.application.port.in.result.SuggestExpertResult;
import org.springframework.stereotype.Component;

@Component
public class SuggestExpertWebMapper {

    public SuggestExpertResponse toWebResponse(SuggestExpertResult suggestExpertResult) {
        return SuggestExpertResponse.builder()
                .expertNo(suggestExpertResult.expertNo())
                .nickname(suggestExpertResult.nickname())
                .profileImageUrl(suggestExpertResult.profileImageUrl())
                .build();
    }
}
