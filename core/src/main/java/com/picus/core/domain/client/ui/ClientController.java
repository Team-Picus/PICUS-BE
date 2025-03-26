package com.picus.core.domain.client.ui;

import com.picus.core.domain.client.application.dto.request.SignUpReq;
import com.picus.core.domain.client.application.usecase.ClientInfoUseCase;
import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.user.application.dto.response.AuthTokenRes;
import com.picus.core.domain.user.application.usecase.TokenBlacklistUseCase;
import com.picus.core.domain.user.domain.entity.UserType;
import com.picus.core.global.config.resolver.annotation.AccessToken;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.jwt.TokenProvider;
import com.picus.core.global.oauth.entity.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientInfoUseCase clientInfoUseCase;
    private final TokenProvider tokenProvider;
    private final TokenBlacklistUseCase tokenBlacklistUseCase;

    @PostMapping
    public AuthTokenRes signUp(@CommonPrincipal UserPrincipal userPrincipal, @AccessToken String accessToken, @RequestBody @Valid SignUpReq request) {
        Client client = clientInfoUseCase.save(userPrincipal.getUserId(), request);
        tokenBlacklistUseCase.invalidateTokens(accessToken);
        return tokenProvider.createToken(client.getId(), UserType.CLIENT);
    }
}
