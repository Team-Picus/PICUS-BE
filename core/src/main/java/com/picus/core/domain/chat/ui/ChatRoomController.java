package com.picus.core.domain.chat.ui;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.application.usecase.ChatRoomMetadataUseCase;
import com.picus.core.global.common.base.BaseResponse;
import com.picus.core.global.config.resolver.annotation.ClientPrincipal;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final ChatRoomMetadataUseCase chatRoomMetadataUseCase;

    // todo: 페이지네이션 완성
    /***
     * 내 채팅방 목록 조회 API
     * @param userPrincipal 채팅방 열람하려는 유저의 정보
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
    public BaseResponse<Long> createChatRoom(@ClientPrincipal UserPrincipal userPrincipal, @RequestParam Long expertNo) {
        return BaseResponse.onSuccess(chatRoomMetadataUseCase.initRoom(userPrincipal.getUserId(), expertNo));
    }
}
