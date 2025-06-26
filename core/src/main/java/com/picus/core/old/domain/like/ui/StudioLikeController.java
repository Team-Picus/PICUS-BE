package com.picus.core.old.domain.like.ui;

import com.picus.core.old.domain.like.domain.service.StudioLikeService;
import com.picus.core.old.global.common.base.BaseResponse;
import com.picus.core.old.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.old.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like/studio")
public class StudioLikeController {

    private final StudioLikeService studioLikeService;

    @PostMapping
    public BaseResponse<Void> like(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam Long studioNo) {
        studioLikeService.save(userPrincipal.getUserId(), studioNo);
        return BaseResponse.onSuccess();
    }

    @DeleteMapping
    public BaseResponse<Void> unlike(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam Long studioNo) {
        studioLikeService.delete(userPrincipal.getUserId(), studioNo);
        return BaseResponse.onSuccess();
    }
}
