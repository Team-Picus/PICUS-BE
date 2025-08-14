package com.picus.core.chat.config;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private static final String BEARER = "Bearer ";
    private static final String TOKEN_HEADER = "Authorization";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 연결 요청시 토큰 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 헤더에서 토큰 꺼내기
            String jwtToken = Optional.ofNullable(accessor.getFirstNativeHeader(TOKEN_HEADER))
                    .filter(t -> t.startsWith(BEARER))
                    .map(t -> t.replace(BEARER, ""))
                    .orElseThrow(() -> {
                        log.error("토큰이 존재하지 않습니다. 세션 ID: {}", accessor.getSessionId());
                        return new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
                    });

//            jwtToken = tokenProvider.createAccessToken("mock", "EXPERT"); // 테스트 용
            // 토큰 검증
            if (tokenProvider.validateToken(jwtToken)) {
                // 검증 성공시 Auth 세팅
                accessor.setUser(tokenProvider.getAuthentication(jwtToken));
                log.info("토큰 인증 성공. 세션 ID: {}", accessor.getSessionId());
            } else {
                // 검증 실패시 예외 반환
                log.error("토큰 인증 실패. 세션 ID: {}", accessor.getSessionId());
                throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
            }
        }

        return message;
    }
}
