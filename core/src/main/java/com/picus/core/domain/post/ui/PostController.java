package com.picus.core.domain.post.ui;

import com.picus.core.domain.post.application.dto.request.PostInitialDto;
import com.picus.core.domain.post.application.dto.response.PostSummaryDto;
import com.picus.core.domain.post.application.usecase.PostUseCase;
import com.picus.core.global.common.base.BaseResponse;
import com.picus.core.global.config.resolver.annotation.ExpertPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostUseCase postUseCase;

    // TODO
    @PostMapping
    public BaseResponse<PostSummaryDto> createPost(@ExpertPrincipal UserPrincipal userPrincipal) {
        PostSummaryDto post = postUseCase.createPost(userPrincipal.getUserId());
        return BaseResponse.onSuccess(post);
    }

    @PostMapping("/initialize")
    public void initializePost(@ExpertPrincipal UserPrincipal userPrincipal,
                               @Valid PostInitialDto postInitialDto) {

        postUseCase.initialPost(userPrincipal.getUserId(), postInitialDto);
    }
}
