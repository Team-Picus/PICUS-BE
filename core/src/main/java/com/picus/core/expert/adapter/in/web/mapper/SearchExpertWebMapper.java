package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import org.springframework.stereotype.Component;

@Component
public class SearchExpertWebMapper {

    public SearchExpertWebResponse toWebResponse(SearchExpertAppResponse searchExpertAppResponse) {
        return SearchExpertWebResponse.builder()
                .expertNo(searchExpertAppResponse.nickname())
                .nickname(searchExpertAppResponse.nickname())
                .profileImageUrl(searchExpertAppResponse.profileImageUrl())
                .build();
    }
}
