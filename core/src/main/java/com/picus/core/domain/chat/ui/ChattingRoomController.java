package com.picus.core.domain.chat.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/{chattingRoomNo}")
public class ChattingRoomController {

    @GetMapping
    public void read(@PathVariable String chattingRoomNo) {
        // 채팅방 목록 최신 순 가져오기
    }

    @DeleteMapping
    public void leave(@PathVariable String chattingRoomNo) {
        // 채팅방 나가기
    }
}
