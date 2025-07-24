package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SuggestExpertWebResponse;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResp;
import org.springframework.stereotype.Component;

@Component
public class SuggestExpertWebMapper {

    public SuggestExpertWebResponse toWebResponse(SuggestExpertAppResp suggestExpertAppResp) {
        return SuggestExpertWebResponse.builder()
                .expertNo(suggestExpertAppResp.expertNo())
                .nickname(suggestExpertAppResp.nickname())
                .profileImageUrl(suggestExpertAppResp.profileImageUrl())
                .build();
    }
}
