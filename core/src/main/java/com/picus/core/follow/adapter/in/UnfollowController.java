package com.picus.core.follow.adapter.in;

import com.picus.core.follow.application.port.in.UnfollowUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class UnfollowController {

    private final UnfollowUseCase unfollowUseCase;

    @DeleteMapping
    public BaseResponse<Void> unfollow(
            @CurrentUser String userNo,
            @RequestParam String expertNo
    ) {
        unfollowUseCase.unfollow(userNo, expertNo);
        return BaseResponse.onSuccess();
    }
}
