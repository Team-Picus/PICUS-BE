package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.response.LoadPostDetailResponse;
import com.picus.core.post.adapter.in.web.mapper.LoadPostDetailWebMapper;
import com.picus.core.post.application.port.in.LoadPostDetailUseCase;
import com.picus.core.post.application.port.in.result.LoadPostDetailResult;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class LoadPostDetailController {

    private final LoadPostDetailUseCase loadPostDetailUseCase;
    private final LoadPostDetailWebMapper webMapper;


    @GetMapping("/{postNo}")
    public BaseResponse<LoadPostDetailResponse> load(@PathVariable String postNo) {
        LoadPostDetailResult result = loadPostDetailUseCase.load(postNo);
        return BaseResponse.onSuccess(webMapper.toResponse(result));
    }
}
