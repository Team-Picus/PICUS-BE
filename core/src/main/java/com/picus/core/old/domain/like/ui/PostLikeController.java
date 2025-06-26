package com.picus.core.old.domain.like.ui;

import com.picus.core.old.domain.like.domain.service.PostLikeService;
import com.picus.core.old.global.common.base.BaseResponse;
import com.picus.core.old.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.old.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like/post")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public BaseResponse<Void> like(@CommonPrincipal UserPrincipal userPrincipal, Long postNo) {
        postLikeService.save(userPrincipal.getUserId(), postNo);
        return BaseResponse.onSuccess();
    }

    @DeleteMapping
    public BaseResponse<Void> unlike(@CommonPrincipal UserPrincipal userPrincipal, Long postNo) {
        postLikeService.delete(userPrincipal.getUserId(), postNo);
        return BaseResponse.onSuccess();
    }
}
