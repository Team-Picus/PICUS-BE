package com.picus.core.old.domain.expert.ui;

import com.picus.core.old.domain.expert.application.dto.request.RegExpReq;
import com.picus.core.old.domain.expert.application.usecase.ExpertInfoUseCase;
import com.picus.core.old.domain.expert.domain.entity.Expert;
import com.picus.core.old.domain.user.application.dto.response.AuthTokenRes;
import com.picus.core.old.domain.user.application.usecase.TokenBlacklistUseCase;
import com.picus.core.old.domain.user.domain.entity.UserType;
import com.picus.core.old.global.config.resolver.annotation.AccessToken;
import com.picus.core.old.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.old.global.jwt.TokenProvider;
import com.picus.core.old.global.oauth.entity.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class ExpertController {

    private final ExpertInfoUseCase expertInfoUseCase;
    private final TokenProvider tokenProvider;
    private final TokenBlacklistUseCase tokenBlacklistUseCase;

    @PostMapping
    public AuthTokenRes registerExpert(@CommonPrincipal UserPrincipal userPrincipal, @AccessToken String accessToken, @RequestBody @Valid RegExpReq request) {
        Expert expert = expertInfoUseCase.save(userPrincipal.getUserId(), request);
        tokenBlacklistUseCase.invalidateTokens(accessToken);
        return tokenProvider.createToken(expert.getId(), UserType.EXPERT);
    }
}
