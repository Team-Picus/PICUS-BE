package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.request.SignUpRequest;
import com.picus.core.user.adapter.in.web.mapper.SignUpWebMapper;
import com.picus.core.user.application.port.in.UserManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
