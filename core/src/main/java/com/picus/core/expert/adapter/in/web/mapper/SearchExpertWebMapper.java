package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.application.port.in.response.SearchExpertAppResp;
import org.springframework.stereotype.Component;

@Component
public class SearchExpertWebMapper {

    public SearchExpertWebResponse toWebResponse(SearchExpertAppResp searchExpertAppResp) {
        return SearchExpertWebResponse.builder()
                .expertNo(searchExpertAppResp.expertNo())
                .nickname(searchExpertAppResp.nickname())
                .profileImageUrl(searchExpertAppResp.profileImageUrl())
                .build();
    }
}
