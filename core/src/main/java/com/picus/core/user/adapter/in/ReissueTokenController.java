package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.annotation.RefreshToken;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.response.TokenReissueResponse;
import com.picus.core.user.adapter.in.web.mapper.TokenReissueWebMapper;
import com.picus.core.user.application.port.in.ReissueTokenUseCase;
import com.picus.core.user.application.port.in.result.ReissueTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class ReissueTokenController {

    private final ReissueTokenUseCase reissueTokenUseCase;
    private final TokenReissueWebMapper webMapper;

    @PostMapping("/reissue")
    public BaseResponse<TokenReissueResponse> reissue(
            @RefreshToken String refreshToken,
            @CurrentUser String userNo
    ) {
        ReissueTokenResult result = reissueTokenUseCase.reissue(refreshToken, userNo);
        return BaseResponse.onSuccess(webMapper.toResponse(result));
    }
}
