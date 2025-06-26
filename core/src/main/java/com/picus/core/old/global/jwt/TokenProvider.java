package com.picus.core.old.global.jwt;

import com.picus.core.old.domain.user.application.dto.response.AuthTokenRes;
import com.picus.core.old.domain.user.domain.entity.UserType;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.oauth.entity.Role;
import com.picus.core.old.global.oauth.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static com.picus.core.old.global.common.exception.code.status.AuthErrorStatus.*;
import static com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;
import static com.picus.core.old.global.oauth.entity.Role.USER;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final CustomUserDetailsService customUserDetailsService;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";
    private static final String ID_CLAIM = "id";
    private static final String ROLE_CLAIM = "role";
    private static final String USER_TYPE_CLAIM = "userType";

    public AuthTokenRes createToken(Long id, UserType userType) {
        return new AuthTokenRes(
                createAccessToken(id, USER, userType),
                createRefreshToken(id, USER)
        );
    }

    private String createAccessToken(Long id, Role role, UserType userType) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(Date.from(
                        LocalDateTime.now()
                                .plusDays(jwtProperties.getAccessTokenExpirationPeriodDay())
                                .atZone(ZoneId.of("Asia/Seoul"))
                                .toInstant()
                ))
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .claim(ID_CLAIM, id)
                .claim(USER_TYPE_CLAIM, userType)
                .claim(ROLE_CLAIM, role.getCode())
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(Long id, Role role) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(Date.from(
                        LocalDateTime.now()
                                .plusMonths(jwtProperties.getRefreshTokenExpirationPeriodMonth())
                                .atZone(ZoneId.of("Asia/Seoul"))
                                .toInstant()
                        )
                )
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .claim(ID_CLAIM, id)
                .claim(ROLE_CLAIM, role.getCode())
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(jwtToken);  // Decode
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Long userNo = getId(token)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(userNo));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public boolean isAccessToken(String token) {
        try {
            return getClaims(token).getSubject().equals(ACCESS_TOKEN_SUBJECT);
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject().equals(ACCESS_TOKEN_SUBJECT);
        } catch (Exception e) {
            throw new RestApiException(_NOT_FOUND); // todo 수정
        }
    }

    public Optional<String> getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(TOKEN_HEADER))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Optional<Long> getId(String token) {
        try {
            return Optional.ofNullable(getClaims(token).get(ID_CLAIM, Long.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserType> getUserType(String token) {
        try {
            return Optional.ofNullable(getClaims(token).get(USER_TYPE_CLAIM, UserType.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Role> getRole(String token) {
        return Optional.of(Role.of(getClaims(token).get(ROLE_CLAIM, String.class)));
    }

    public LocalDateTime getExpiration(String token) {
        try {
            return getClaims(token).getExpiration()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (ExpiredJwtException e) {
            throw new RestApiException(EXPIRED_MEMBER_JWT);
        } catch (Exception e) {
            throw new RestApiException(UNSUPPORTED_JWT); // todo 수정
        }
    }

    public boolean isExpired(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);  // Decode
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            throw new RestApiException(_NOT_FOUND); // todo 수정
        }
    }
}
