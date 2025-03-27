package com.picus.core.domain.like.ui;

import com.picus.core.domain.like.domain.service.StudioLikeService;
import com.picus.core.global.common.base.BaseResponse;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like/studio")
public class StudioLikeController {

    private final StudioLikeService studioLikeService;

    public BaseResponse<Void> like(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam Long studioNo) {
        studioLikeService.save(userPrincipal.getUserId(), studioNo);
        return BaseResponse.onSuccess();
    }

    public BaseResponse<Void> unlike(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam Long studioNo) {
        studioLikeService.delete(userPrincipal.getUserId(), studioNo);
        return BaseResponse.onSuccess();
    }
}
