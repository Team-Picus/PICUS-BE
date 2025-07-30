package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.response.LoadProfileResponse;
import com.picus.core.user.adapter.in.web.mapper.SignUpWebMapper;
import com.picus.core.user.application.port.in.LoadProfileUseCase;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/profile")
public class LoadProfileController {

    private final LoadProfileUseCase loadProfileUseCase;
    private final SignUpWebMapper webMapper;

    @GetMapping("/me")
    public BaseResponse<LoadProfileResponse> getMyProfile(
            @CurrentUser String userNo
    ) {
        User user = loadProfileUseCase.load(userNo);
        // todo: 배포 직전 PRESIGNED URI 가져오기
        return BaseResponse.onSuccess(webMapper.toResponse(user, null));
    }

}
