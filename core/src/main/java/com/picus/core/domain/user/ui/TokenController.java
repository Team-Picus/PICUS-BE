package com.picus.core.domain.user.ui;

import com.picus.core.global.config.resolver.annotation.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {

    @PostMapping
    public String reissueToken(@RefreshToken String refreshToken) {
        return refreshToken;
    }
}
