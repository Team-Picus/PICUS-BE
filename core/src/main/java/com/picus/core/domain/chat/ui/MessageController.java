package com.picus.core.domain.chat.ui;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.application.usecase.MessageHistoryUseCase;
import com.picus.core.domain.chat.application.usecase.SendMessageUseCase;
import com.picus.core.domain.chat.application.usecase.SessionManagementUseCase;
import com.picus.core.global.common.base.BaseResponse;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SendMessageUseCase sendMessageUseCase;
    private final MessageHistoryUseCase messageHistoryUseCase;
    private final SessionManagementUseCase sessionManagementUseCase;

    /***
     * STOMP 실시간 채팅 Destination Queue: /pub/chat.message를 통해 호출 후 처리 되는 로직
     * @param accessor
     * @param message
     * @return
     */
    @MessageMapping("chat.message")
    public BaseResponse<Void> sendMessage(StompHeaderAccessor accessor, SendMsgReq message) {
        sendMessageUseCase.sendMessage(accessor, message);
        return BaseResponse.onSuccess();
    }

    /***
     * 채팅방 입장 시 채팅 이력 조회 API
     * @param roomNo
     * @return Disconnect 할 때 저장해둔 lastEntryTime + 이전 10개 기준으로 불러오기
     */
    @PatchMapping("/api/v1/chat/{roomNo}/message")
    public BaseResponse<List<MessageRes>> readMessages(@CommonPrincipal UserPrincipal userPrincipal, @PathVariable Long roomNo, @RequestParam("last") Optional<String> lastMessageNo) {
        return BaseResponse.onSuccess(messageHistoryUseCase.readMessages(roomNo, userPrincipal.getUserId(), lastMessageNo));
    }

    /***
     * 채팅 탭 입장 (채팅방 목록 조회 - WebSocket 연결)
     * @param event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessionManagementUseCase.handleConnectMessage(accessor);
    }

    /***
     * 채팅 탭 퇴장 (화면 전환 - WebSocket 해제)
     * @param event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessionManagementUseCase.handleDisconnectMessage(accessor);
    }
}
