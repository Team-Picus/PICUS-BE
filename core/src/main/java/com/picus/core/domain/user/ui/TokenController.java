package com.picus.core.domain.user.ui;

import com.picus.core.domain.user.application.dto.response.AuthTokenRes;
import com.picus.core.domain.user.application.usecase.TokenBlacklistUseCase;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.config.resolver.annotation.RefreshToken;
import com.picus.core.global.jwt.TokenProvider;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {


    private final TokenBlacklistUseCase tokenBlacklistUseCase;
    private final TokenProvider tokenProvider;

    @PostMapping
    public AuthTokenRes reissueToken(@CommonPrincipal UserPrincipal userPrincipal, @RefreshToken String refreshToken) {
        tokenBlacklistUseCase.invalidateRefreshToken(refreshToken);
        return tokenProvider.createToken(userPrincipal.getUserId(), userPrincipal.getUserType());
    }
}
