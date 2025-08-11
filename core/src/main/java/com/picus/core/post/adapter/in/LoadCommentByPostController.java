package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.response.LoadCommentByPostResponse;
import com.picus.core.post.adapter.in.web.mapper.LoadCommentByPostWebMapper;
import com.picus.core.post.application.port.in.LoadCommentByPostUseCase;
import com.picus.core.post.application.port.in.result.LoadCommentByPostResult;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class LoadCommentByPostController {

    private final LoadCommentByPostUseCase loadCommentByPostUseCase;

    private final LoadCommentByPostWebMapper webMapper;

    @GetMapping("/{post_no}/comments")
    public BaseResponse<LoadCommentByPostResponse> load(@PathVariable("post_no") String postNo) {
        List<LoadCommentByPostResult> results = loadCommentByPostUseCase.load(postNo);

        return BaseResponse.onSuccess(webMapper.toResponse(results));
    }
}
