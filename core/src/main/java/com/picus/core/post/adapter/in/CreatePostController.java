package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest;
import com.picus.core.post.adapter.in.web.mapper.CreatePostWebMapper;
import com.picus.core.post.application.port.in.CreatePostUseCase;
import com.picus.core.post.application.port.in.command.CreatePostCommand;
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
    public BaseResponse<Void> createPost(@RequestBody @Valid CreatePostRequest webReq, @CurrentUser String userNo) {
        CreatePostCommand appReq = createPostWebMapper.toCommand(webReq, userNo);
        createPostUseCase.create(appReq);
        return BaseResponse.onSuccess();
    }
}
