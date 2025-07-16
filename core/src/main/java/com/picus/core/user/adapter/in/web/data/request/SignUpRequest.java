package com.picus.core.user.adapter.in.web.data.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest (
        @NotBlank String nickname,

        // 소셜로그인 시 기입이 안됐을 경우만
        String tel,
        String email
) {}
