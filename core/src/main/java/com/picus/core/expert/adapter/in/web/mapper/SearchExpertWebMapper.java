package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.application.port.in.response.SearchExpertResult;
import org.springframework.stereotype.Component;

@Component
public class SearchExpertWebMapper {

    public SearchExpertWebResponse toWebResponse(SearchExpertResult searchExpertResult) {
        return SearchExpertWebResponse.builder()
                .expertNo(searchExpertResult.expertNo())
                .nickname(searchExpertResult.nickname())
                .profileImageUrl(searchExpertResult.profileImageUrl())
                .build();
    }
}
