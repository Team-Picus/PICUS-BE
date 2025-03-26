package com.picus.core.global.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class JwtProperties {
    @Value("${jwt.secret}")
    private String key;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriodDay;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriodMonth;
}
