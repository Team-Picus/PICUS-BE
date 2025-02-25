package com.picus.core.domain.chat.ui;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.ReadMsgRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/{chattingRoomNo}")
public class MessageController {

    @PostMapping("/message")
    public void sendMessage(@PathVariable Long chattingRoomNo, @RequestBody SendMsgReq dto) {

    }

    @PatchMapping("/message")
    public Page<ReadMsgRes> readMessages(@PathVariable Long chattingRoomNo) {
        // 이전까지 모든 채팅들 중 "isRead = false"인 row 모두 true로 변환
        return null;
    }
}
