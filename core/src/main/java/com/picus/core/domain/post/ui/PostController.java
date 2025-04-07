package com.picus.core.domain.post.ui;

import com.picus.core.domain.post.application.dto.request.PostInitial;
import com.picus.core.domain.post.application.dto.response.PostDetailResponse;
import com.picus.core.domain.post.application.dto.response.PostSummaryResponse;
import com.picus.core.domain.post.application.usecase.PostManagementUseCase;
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

    private final PostManagementUseCase postManagementUseCase;

    @PostMapping
    public BaseResponse<PostSummaryResponse> createPost(@ExpertPrincipal UserPrincipal userPrincipal) {
        PostSummaryResponse post = postManagementUseCase.createPost(userPrincipal.getUserId());
        return BaseResponse.onSuccess(post);
    }

    @PostMapping("/register")
    public BaseResponse<PostDetailResponse> initializePost(@ExpertPrincipal UserPrincipal userPrincipal,
                                                           @Valid PostInitial postInitial) {

        PostDetailResponse postDetailDto = postManagementUseCase.registerPost(userPrincipal.getUserId(), postInitial);
        return BaseResponse.onSuccess(postDetailDto);
    }

    @PostMapping("/{postId}")
    public BaseResponse<PostDetailResponse> findPostDetail(@CommonPrincipal UserPrincipal userPrincipal,
                                                           @PathVariable Long postId,
                                                           @CheckViewHistory boolean isNewView) {
        PostDetailResponse postDetailDto = postManagementUseCase.findPostDetail(postId, isNewView);
        return BaseResponse.onSuccess(postDetailDto);
    }


    // TODO 가격별, 지역(카테고리), 장소(카테고리), 테마(카테고리) 별 조회
    @PostMapping("/list")
    public BaseResponse<PostDetailResponse> findPosts(@ExpertPrincipal UserPrincipal userPrincipal,
                                                      @Valid PostInitial postInitial) {
        return null;
    }

}
