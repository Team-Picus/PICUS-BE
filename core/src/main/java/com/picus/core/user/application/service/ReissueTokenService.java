package com.picus.core.user.application.service;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.ReissueTokenUseCase;
import com.picus.core.user.application.port.in.ValidateTokenUseCase;
import com.picus.core.user.application.port.in.result.ReissueTokenResult;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.AuthErrorStatus.INVALID_ACCESS_TOKEN;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {

    private final UserReadPort userReadPort;
    private final TokenProvider tokenProvider;
    private final ValidateTokenUseCase validateTokenUseCase;

    @Override
    public ReissueTokenResult reissue(String refreshToken, String userNo) {
        if (validateTokenUseCase.isValidRefreshToken(userNo, refreshToken))
            throw new RestApiException(INVALID_ACCESS_TOKEN);

        Role role = userReadPort.findRoleById(userNo);
        String accessToken = tokenProvider.createAccessToken(userNo, role.getName());

        return ReissueTokenResult.builder()
                .accessToken(accessToken)
                .build();
    }
}
