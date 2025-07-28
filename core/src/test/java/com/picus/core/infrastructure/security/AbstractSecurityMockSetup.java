package com.picus.core.infrastructure.security;

import com.picus.core.infrastructure.security.jwt.ExcludeBlacklistPathProperties;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.user.application.port.in.TokenValidationQueryPort;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public abstract class AbstractSecurityMockSetup {

    public static final String TEST_USER_ID = "test_user_id";

    @MockitoBean
    protected TokenProvider tokenProvider;

    @MockitoBean
    protected TokenValidationQueryPort tokenValidationQueryPort;

    @MockitoBean
    protected ExcludeBlacklistPathProperties excludeBlacklistPathProperties;

    @BeforeEach
    void setupSecurityMocks() {
        given(tokenProvider.getToken(any())).willReturn(Optional.of("mock-jwt-token"));
        given(tokenProvider.getId(any())).willReturn(Optional.of(TEST_USER_ID));
        given(tokenProvider.validateToken("mock-jwt-token")).willReturn(true);
        given(tokenProvider.getAuthentication("mock-jwt-token"))
                .willReturn(new UsernamePasswordAuthenticationToken("user", null, List.of()));
    }
}