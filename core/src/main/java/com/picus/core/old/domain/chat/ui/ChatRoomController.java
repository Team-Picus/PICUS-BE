package com.picus.core.old.domain.chat.ui;

import com.picus.core.old.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.old.domain.chat.application.usecase.ChatRoomMetadataUseCase;
import com.picus.core.old.global.common.base.BaseResponse;
import com.picus.core.old.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.old.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final ChatRoomMetadataUseCase chatRoomMetadataUseCase;

    // todo: 페이지네이션 완성
    /***
     * 내 채팅방 목록 조회 API
     * @param userPrincipal 유저 정보
     * @param last 커서 기반 페이지네이션
     * @return 최근 메세지 날짜 내림차순 20개 채팅방 조회
     */
    @GetMapping
    public BaseResponse<List<ChatRoomRes>> readChatRooms(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam(required = false) Long last) {
        return BaseResponse.onSuccess(chatRoomMetadataUseCase.readChatRooms(userPrincipal.getUserId(), last));
    }

    /***
     * 채팅방 생성
     * @param userPrincipal 고객 정보
     * @param expertNo 문의하려는 전문가 인덱스
     * @return 채팅방 인덱스
     */
    @PostMapping
    public BaseResponse<Long> createChatRoom(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam Long expertNo) {
        return BaseResponse.onSuccess(chatRoomMetadataUseCase.initRoom(userPrincipal.getUserId(), expertNo));
    }
}
