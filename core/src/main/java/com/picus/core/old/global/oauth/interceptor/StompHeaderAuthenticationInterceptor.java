package com.picus.core.old.global.oauth.interceptor;

import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.jwt.TokenProvider;
import com.picus.core.old.global.utils.StompHeaderAccessorUtil;
import com.picus.core.old.global.common.exception.code.status.AuthErrorStatus;
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
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == CONNECT) {
            String token = stompHeaderAccessorUtil.extractToken(accessor);

            Long memberId = tokenProvider.getId(token)
                    .orElseThrow(() -> new RestApiException(AuthErrorStatus.EMPTY_JWT));
            stompHeaderAccessorUtil.setMemberIdInSession(accessor, memberId);

            Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInHeader(accessor);
            stompHeaderAccessorUtil.setChatRoomIdInSession(accessor, chatRoomId);
        }

        return message;
    }
}
