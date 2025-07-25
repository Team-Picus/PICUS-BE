package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.adapter.in.web.data.response.SuggestExpertWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.SuggestExpertWebMapper;
import com.picus.core.expert.application.port.in.SuggestExpertsQuery;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResponse;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SuggestExpertController {

    private final SuggestExpertsQuery suggestExpertsQuery;
    private final SuggestExpertWebMapper suggestExpertWebMapper;

    /**
     * 전문가 검색어 추천 API
     */
    @GetMapping("/api/v1/experts/search/suggestions")
    public BaseResponse<List<SuggestExpertWebResponse>> searchExpert(
            @RequestParam String keyword, @RequestParam(required = false, defaultValue = "3") int size) {
        List<SuggestExpertAppResponse> suggestExpertAppResponses = suggestExpertsQuery.suggestExperts(keyword, size);

        return BaseResponse.onSuccess(
                suggestExpertAppResponses.stream()
                        .map(suggestExpertWebMapper::toWebResponse)
                        .toList()
        );
    }

}
