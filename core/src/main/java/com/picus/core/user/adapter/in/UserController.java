package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.AccessToken;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.application.port.in.TokenManagementCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final TokenManagementCommandPort tokenManagementCommandPort;

    @DeleteMapping("/logout")
    public BaseResponse<Void> logout(@AccessToken String accessToken, @CurrentUser String userNo) {
        tokenManagementCommandPort.blacklist(userNo, accessToken);
        return BaseResponse.onSuccess();
    }
}
