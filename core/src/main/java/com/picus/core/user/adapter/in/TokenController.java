package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.annotation.RefreshToken;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.response.TokenReissueResponse;
import com.picus.core.user.adapter.in.web.mapper.TokenReissueWebMapper;
import com.picus.core.user.application.port.in.TokenManagementCommandPort;
import com.picus.core.user.application.port.in.TokenValidationQueryPort;
import com.picus.core.user.application.port.in.UserManagementUseCase;
import com.picus.core.user.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {

    private final TokenReissueWebMapper tokenReissueWebMapper;
    private final TokenManagementCommandPort tokenManagementCommandPort;
    private final TokenValidationQueryPort tokenValidationQueryPort;
    private final UserManagementUseCase userManagementUseCase;

    @PostMapping("/token/reissue")
    public BaseResponse<TokenReissueResponse> reissue(@RefreshToken String refreshToken, @CurrentUser String userNo) {
        tokenValidationQueryPort.validate(userNo, refreshToken);
        Role role = userManagementUseCase.findRoleById(userNo);
        String reissuedToken = tokenManagementCommandPort.reissue(userNo, role);
        return BaseResponse.onSuccess(tokenReissueWebMapper.toResponse(reissuedToken));
    }
}
