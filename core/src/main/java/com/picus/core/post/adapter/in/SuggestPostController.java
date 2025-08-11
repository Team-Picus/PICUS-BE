package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.response.SuggestPostResponse;
import com.picus.core.post.adapter.in.web.mapper.SuggestPostWebMapper;
import com.picus.core.post.application.port.in.SuggestPostUseCase;
import com.picus.core.post.application.port.in.result.SuggestPostResult;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class SuggestPostController {

    private final SuggestPostUseCase suggestPostUseCase;
    private final SuggestPostWebMapper webMapper;

    @GetMapping("/search/suggestions")
    public BaseResponse<List<SuggestPostResponse>> suggestPosts(@RequestParam String keyword, @RequestParam(defaultValue = "5") int size) {
        List<SuggestPostResult> results = suggestPostUseCase.suggest(keyword, size);

        List<SuggestPostResponse> responses = results.stream()
                .map(webMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(responses);
    }
}
