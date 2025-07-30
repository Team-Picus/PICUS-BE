package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.response.SuggestPostsResponse;
import com.picus.core.post.adapter.in.web.mapper.SuggestPostsWebMapper;
import com.picus.core.post.application.port.in.SuggestPostsUseCase;
import com.picus.core.post.application.port.in.result.SuggestPostsResult;
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
public class SuggestPostsController {

    private final SuggestPostsUseCase suggestPostsUseCase;
    private final SuggestPostsWebMapper webMapper;

    @GetMapping("/search/suggestions")
    public BaseResponse<List<SuggestPostsResponse>> suggestPosts(@RequestParam String keyword, @RequestParam(defaultValue = "5") int size) {
        List<SuggestPostsResult> results = suggestPostsUseCase.suggest(keyword, size);

        List<SuggestPostsResponse> responses = results.stream()
                .map(webMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(responses);
    }
}
