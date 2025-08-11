package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertResponse;
import com.picus.core.expert.application.port.in.result.SearchExpertResult;
import org.springframework.stereotype.Component;

@Component
public class SearchExpertWebMapper {

    public SearchExpertResponse toResponse(SearchExpertResult searchExpertResult) {
        return SearchExpertResponse.builder()
                .expertNo(searchExpertResult.expertNo())
                .nickname(searchExpertResult.nickname())
                .profileImageUrl(searchExpertResult.profileImageUrl())
                .build();
    }
}
