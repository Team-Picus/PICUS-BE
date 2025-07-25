package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.request.SignUpRequest;
import com.picus.core.user.adapter.in.web.data.response.GetProfileResponse;
import com.picus.core.user.adapter.in.web.mapper.SignUpWebMapper;
import com.picus.core.user.application.port.in.UserManagementUseCase;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/profile")
public class UserProfileController {

    private final UserManagementUseCase userManagementUseCase;
    private final SignUpWebMapper webMapper;

    @PatchMapping("/complete")
    public BaseResponse<Void> signUp(@CurrentUser String userNo, @RequestBody SignUpRequest request) {
        userManagementUseCase.completeProfile(webMapper.toCommand(userNo, request));
        return BaseResponse.onSuccess();
    }

    @GetMapping("/me")
    public BaseResponse<GetProfileResponse> getMyProfile(@CurrentUser String userNo) {
        User user = userManagementUseCase.findById(userNo);
        // todo: 배포 직전 PRESIGNED URI 가져오기
        return BaseResponse.onSuccess(webMapper.toDto(user, null));
    }
}
