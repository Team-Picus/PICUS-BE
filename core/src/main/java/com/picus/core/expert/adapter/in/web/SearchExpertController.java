package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.SearchExpertWebMapper;
import com.picus.core.expert.application.port.in.SearchExpertsQuery;
import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SearchExpertController {

    private final SearchExpertsQuery searchExpertsQuery;
    private final SearchExpertWebMapper searchExpertWebMapper;

    /**
     * 전문가 검색 API
     */
    @GetMapping("/api/v1/experts/search/results")
    public BaseResponse<List<SearchExpertWebResponse>> searchExpert(@RequestParam String keyword) {
        List<SearchExpertAppResponse> searchExpertAppResponses = searchExpertsQuery.searchExperts(keyword);

        return BaseResponse.onSuccess(
                searchExpertAppResponses.stream()
                        .map(searchExpertWebMapper::toWebResponse)
                        .toList()
        );
    }
}
