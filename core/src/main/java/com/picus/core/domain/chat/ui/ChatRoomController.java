package com.picus.core.domain.chat.ui;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.application.usecase.ChatRoomMetadataUseCase;
import com.picus.core.global.common.base.BaseResponse;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final ChatRoomMetadataUseCase chatRoomMetadataUseCase;

    // todo: 커서 기반 페이지네이션
    /***
     * 내 채팅방 목록 조회 API
     * @param userPrincipal
     * @param page
     * @return 최근 메세지 날짜 내림차순 20개 채팅방 조회
     */
    @GetMapping
    public BaseResponse<Page<ChatRoomRes>> readChatRooms(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam Integer page) {
        return BaseResponse.onSuccess(chatRoomMetadataUseCase.readChatRooms(userPrincipal, page));
    }

    /***    todo: 요구사항 변경
     * 채팅방 나가기 API
     * @param userPrincipal
     * @param roomNo
     */
    @DeleteMapping("/{roomNo}")
    public BaseResponse<Void> leave(@CommonPrincipal UserPrincipal userPrincipal, @PathVariable Long roomNo) {
        chatRoomMetadataUseCase.leaveChatRoom(userPrincipal.getUserId(), roomNo);
        return BaseResponse.onSuccess();
    }

    @PostMapping
    public BaseResponse<Long> initRoomTest(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam Long expertNo) {
        return BaseResponse.onSuccess(chatRoomMetadataUseCase.initRoom(userPrincipal.getUserId(), expertNo));
    }
}
