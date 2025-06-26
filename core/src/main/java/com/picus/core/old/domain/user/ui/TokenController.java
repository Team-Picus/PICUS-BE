package com.picus.core.old.domain.user.ui;

import com.picus.core.old.domain.user.application.dto.response.AuthTokenRes;
import com.picus.core.old.domain.user.application.usecase.TokenBlacklistUseCase;
import com.picus.core.old.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.old.global.config.resolver.annotation.RefreshToken;
import com.picus.core.old.global.jwt.TokenProvider;
import com.picus.core.old.global.oauth.entity.UserPrincipal;
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
