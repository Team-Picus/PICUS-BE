package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SuggestExpertWebResponse;
import com.picus.core.expert.application.port.in.response.SuggestExpertResult;
import org.springframework.stereotype.Component;

@Component
public class SuggestExpertWebMapper {

    public SuggestExpertWebResponse toWebResponse(SuggestExpertResult suggestExpertResult) {
        return SuggestExpertWebResponse.builder()
                .expertNo(suggestExpertResult.expertNo())
                .nickname(suggestExpertResult.nickname())
                .profileImageUrl(suggestExpertResult.profileImageUrl())
                .build();
    }
}
