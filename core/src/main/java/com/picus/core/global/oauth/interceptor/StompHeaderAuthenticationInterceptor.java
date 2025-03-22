package com.picus.core.global.oauth.interceptor;

import com.picus.core.global.oauth.token.AuthToken;
import com.picus.core.global.oauth.token.AuthTokenProvider;
import com.picus.core.global.utils.StompHeaderAccessorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

@Component
@RequiredArgsConstructor
public class StompHeaderAuthenticationInterceptor implements ChannelInterceptor {

    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;
    private final AuthTokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == CONNECT) {
            String token = stompHeaderAccessorUtil.extractToken(accessor);

            AuthToken authToken = tokenProvider.convertAuthToken(token);
            Long memberId = Long.valueOf(authToken.getTokenClaims().getSubject());
            stompHeaderAccessorUtil.setMemberIdInSession(accessor, memberId);

            Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInHeader(accessor);
            stompHeaderAccessorUtil.setChatRoomIdInSession(accessor, chatRoomId);
        }

        return message;
    }
}
