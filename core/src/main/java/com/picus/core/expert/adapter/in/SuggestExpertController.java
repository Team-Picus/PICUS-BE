package com.picus.core.expert.adapter.in;

import com.picus.core.expert.adapter.in.web.data.response.SuggestExpertWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.SuggestExpertWebMapper;
import com.picus.core.expert.application.port.in.SuggestExpertsUseCase;
import com.picus.core.expert.application.port.in.response.SuggestExpertResult;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class SuggestExpertController {

    private final SuggestExpertsUseCase suggestExpertsUseCase;
    private final SuggestExpertWebMapper suggestExpertWebMapper;

    /**
     * 전문가 검색어 추천 API
     */
    @GetMapping("/search/suggestions")
    public BaseResponse<List<SuggestExpertWebResponse>> searchExpert(
            @RequestParam String keyword, @RequestParam(required = false, defaultValue = "3") int size) {
        List<SuggestExpertResult> suggestExpertAppResponse = suggestExpertsUseCase.suggestExperts(keyword, size);

        return BaseResponse.onSuccess(
                suggestExpertAppResponse.stream()
                        .map(suggestExpertWebMapper::toWebResponse)
                        .toList()
        );
    }

}
