package com.picus.core.domain.post.ui;

import com.picus.core.domain.post.application.dto.request.PostInitialDto;
import com.picus.core.domain.post.application.dto.response.PostDetailDto;
import com.picus.core.domain.post.application.dto.response.PostSummaryDto;
import com.picus.core.domain.post.application.usecase.PostUseCase;
import com.picus.core.global.common.base.BaseResponse;
import com.picus.core.global.config.resolver.annotation.CheckViewHistory;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.config.resolver.annotation.ExpertPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostUseCase postUseCase;

    @PostMapping
    public BaseResponse<PostSummaryDto> createPost(@ExpertPrincipal UserPrincipal userPrincipal) {
        PostSummaryDto post = postUseCase.createPost(userPrincipal.getUserId());
        return BaseResponse.onSuccess(post);
    }

    @PostMapping("/register")
    public BaseResponse<PostDetailDto> initializePost(@ExpertPrincipal UserPrincipal userPrincipal,
                               @Valid PostInitialDto postInitialDto) {

        PostDetailDto postDetailDto = postUseCase.registerPost(userPrincipal.getUserId(), postInitialDto);
        return BaseResponse.onSuccess(postDetailDto);
    }

    @PostMapping("/{postId}")
    public BaseResponse<PostDetailDto> findPostDetail(@CommonPrincipal UserPrincipal userPrincipal,
                                                      @PathVariable Long postId,
                                                      @CheckViewHistory boolean isNewView) {
        PostDetailDto postDetailDto = postUseCase.findPostDetail(postId, isNewView);
        return BaseResponse.onSuccess(postDetailDto);
    }


    // TODO 가격별, 지역(카테고리), 장소(카테고리), 테마(카테고리) 별 조회
    @PostMapping("/list")
    public BaseResponse<PostDetailDto> findPosts(@ExpertPrincipal UserPrincipal userPrincipal,
                                                      @Valid PostInitialDto postInitialDto) {
        return null;
    }

}
