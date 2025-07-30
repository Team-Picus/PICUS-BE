package com.picus.core.follow.adapter.in;

import com.picus.core.follow.application.port.in.FollowUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowUseCase followUseCase;

    @PostMapping
    public BaseResponse<Void> follow(
            @CurrentUser String userNo,
            @RequestParam String expertNo
    ) {
        followUseCase.follow(userNo, expertNo);
        return BaseResponse.onSuccess();
    }
}
