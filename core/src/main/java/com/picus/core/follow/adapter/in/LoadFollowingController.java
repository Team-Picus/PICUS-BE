package com.picus.core.follow.adapter.in;

import com.picus.core.follow.adapter.in.web.data.response.LoadFollowResponse;
import com.picus.core.follow.adapter.in.web.mapper.FollowWebMapper;
import com.picus.core.follow.application.port.in.LoadFollowingUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class LoadFollowingController {

    private final LoadFollowingUseCase loadFollowingUseCase;
    private final FollowWebMapper followWebMapper;

    @GetMapping
    public BaseResponse<List<LoadFollowResponse>> getFollows(
            @CurrentUser String userNo
    ) {
        List<LoadFollowResponse> response = loadFollowingUseCase.getFollows(userNo).stream()
                .map(followWebMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(response);
    }
}
