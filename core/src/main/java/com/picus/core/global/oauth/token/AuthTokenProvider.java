package com.picus.core.global.oauth.token;

import com.picus.core.domain.user.domain.entity.UserType;
import com.picus.core.global.oauth.entity.Provider;
import com.picus.core.global.oauth.entity.Role;
import com.picus.core.global.oauth.entity.UserPrincipal;
import com.picus.core.global.oauth.exception.TokenValidFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class AuthTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role, String userType, Date expiry) {
        String token = Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .claim("userType", userType) // 추가된 클레임
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(key)
                .compact();
        return new AuthToken(token, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {
        if (authToken.validate()) {
            Claims claims = authToken.getTokenClaims();
            String roleClaim = claims.get(AUTHORITIES_KEY).toString();
            String userTypeString = claims.get("userType", String.class);
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{roleClaim})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            // UserPrincipal에 userType을 포함하여 생성
            UserPrincipal principal = new UserPrincipal(
                    Long.valueOf(claims.getSubject()),
                    Provider.KAKAO, // provider 정보가 필요하다면 토큰에 넣거나 다른 방식으로 주입
                    Role.of(roleClaim),
                    (Collection<GrantedAuthority>) authorities,
                    UserType.valueOf(userTypeString)
            );
            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }
}
