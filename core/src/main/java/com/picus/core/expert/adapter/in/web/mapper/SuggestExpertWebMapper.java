package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.SuggestExpertWebResponse;
import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResponse;
import org.springframework.stereotype.Component;

@Component
public class SuggestExpertWebMapper {

    public SuggestExpertWebResponse toWebResponse(SuggestExpertAppResponse suggestExpertAppResponse) {
        return SuggestExpertWebResponse.builder()
                .expertNo(suggestExpertAppResponse.expertNo())
                .nickname(suggestExpertAppResponse.nickname())
                .profileImageUrl(suggestExpertAppResponse.profileImageUrl())
                .build();
    }
}
