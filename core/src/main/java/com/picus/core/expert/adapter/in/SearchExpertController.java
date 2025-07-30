package com.picus.core.expert.adapter.in;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertResponse;
import com.picus.core.expert.adapter.in.web.mapper.SearchExpertWebMapper;
import com.picus.core.expert.application.port.in.SearchExpertsUseCase;
import com.picus.core.expert.application.port.in.result.SearchExpertResult;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/experts")
public class SearchExpertController {

    private final SearchExpertsUseCase searchExpertsUseCase;
    private final SearchExpertWebMapper searchExpertWebMapper;

    /**
     * 전문가 검색 API
     */
    @GetMapping("/search/results")
    public BaseResponse<List<SearchExpertResponse>> searchExpert(@RequestParam String keyword) {
        List<SearchExpertResult> searchExpertAppRespons = searchExpertsUseCase.searchExperts(keyword);

        return BaseResponse.onSuccess(
                searchExpertAppRespons.stream()
                        .map(searchExpertWebMapper::toWebResponse)
                        .toList()
        );
    }
}
