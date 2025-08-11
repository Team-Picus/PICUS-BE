package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.response.LoadRandomPostResponse;
import com.picus.core.post.adapter.in.web.mapper.LoadRandomPostWebMapper;
import com.picus.core.post.application.port.in.LoadRandomPostUseCase;
import com.picus.core.post.application.port.in.result.LoadRandomPostResult;
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
public class LoadRandomPostController {

    private final LoadRandomPostUseCase loadRandomPostUseCase;

    private final LoadRandomPostWebMapper webMapper;

    @GetMapping("/random")
    public BaseResponse<List<LoadRandomPostResponse>> load(@RequestParam(defaultValue = "5") int size) {
        List<LoadRandomPostResult> results = loadRandomPostUseCase.load(size);

        return BaseResponse.onSuccess(
                results.stream()
                        .map(webMapper::toResponse)
                        .toList()
        );
    }
}
