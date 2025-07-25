package com.picus.core.follow.adapter.in;

import com.picus.core.follow.adapter.in.web.data.response.GetFollowResponse;
import com.picus.core.follow.adapter.in.web.mapper.FollowWebMapper;
import com.picus.core.follow.application.port.in.FollowManagementUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowManagementUseCase followManagementUseCase;
    private final FollowWebMapper followWebMapper;

    @PostMapping
    public BaseResponse<Void> follow(@CurrentUser String userNo, @RequestParam String expertNo) {
        followManagementUseCase.follow(userNo, expertNo);
        return BaseResponse.onSuccess();
    }

    @DeleteMapping
    public BaseResponse<Void> unfollow(@CurrentUser String userNo, @RequestParam String expertNo) {
        followManagementUseCase.unfollow(userNo, expertNo);
        return BaseResponse.onSuccess();
    }

    @GetMapping
    public BaseResponse<List<GetFollowResponse>> getFollows(@CurrentUser String userNo) {
        List<GetFollowResponse> response = followManagementUseCase.getFollows(userNo).stream()
                .map(followWebMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(response);
    }
}
