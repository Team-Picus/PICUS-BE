package com.picus.core.domain.chat.ui;

import com.picus.core.domain.chat.application.dto.response.ReadMsgRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/{roomNo}")
public class ChatRoomController {

    @GetMapping
    public void read(@PathVariable String roomNo) {
        // 채팅방 목록 최신 순 가져오기
    }

    @PatchMapping("/message")
    public Page<ReadMsgRes> readMessages(@PathVariable Long roomNo) {
        // 이전까지 모든 채팅들 중 "isRead = false"인 row 모두 true로 변환
        return null;
    }

    @DeleteMapping
    public void leave(@PathVariable String roomNo) {
        // 채팅방 나가기
    }
}
