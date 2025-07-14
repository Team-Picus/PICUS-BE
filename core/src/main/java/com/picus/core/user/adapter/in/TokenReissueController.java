package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.AccessToken;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.annotation.RefreshToken;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.response.TokenReissueResponse;
import com.picus.core.user.adapter.in.web.mapper.TokenReissueWebMapper;
import com.picus.core.user.application.port.in.TokenManagementCommand;
import com.picus.core.user.application.port.in.TokenValidationQuery;
import com.picus.core.user.application.port.in.UserManagementUseCase;
import com.picus.core.user.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenReissueController {

    private final TokenReissueWebMapper tokenReissueWebMapper;
    private final TokenManagementCommand tokenManagementCommand;
    private final TokenValidationQuery tokenValidationQuery;
    private final UserManagementUseCase userManagementUseCase;

    @PostMapping("/reissue")
    public BaseResponse<TokenReissueResponse> reissue(@RefreshToken String refreshToken, @CurrentUser String userNo) {
        tokenValidationQuery.validate(userNo, refreshToken);
        Role role = userManagementUseCase.findRoleById(userNo);
        String reissuedToken = tokenManagementCommand.reissue(userNo, role);
        return BaseResponse.onSuccess(tokenReissueWebMapper.mapToTokenReissueResponse(reissuedToken));
    }

    @DeleteMapping("/logout")
    public BaseResponse<Void> logout(@AccessToken String accessToken, @CurrentUser String userNo) {
        tokenManagementCommand.blacklist(userNo, accessToken);
        return BaseResponse.onSuccess();
    }
}
