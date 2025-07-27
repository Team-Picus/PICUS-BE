package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.request.CreatePostWebReq;
import com.picus.core.post.adapter.in.web.mapper.CreatePostWebMapper;
import com.picus.core.post.application.port.in.CreatePostUseCase;
import com.picus.core.post.application.port.in.request.CreatePostAppReq;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class CreatePostController {

    private final CreatePostUseCase createPostUseCase;
    private final CreatePostWebMapper createPostWebMapper;

    @PostMapping
    public BaseResponse<Void> createPost(@RequestBody @Valid CreatePostWebReq webReq, @CurrentUser String userNo) {
        CreatePostAppReq appReq = createPostWebMapper.toAppReq(webReq, userNo);
        createPostUseCase.create(appReq);
        return BaseResponse.onSuccess();
    }
}
