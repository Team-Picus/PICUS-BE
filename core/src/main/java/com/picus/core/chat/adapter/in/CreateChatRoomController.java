package com.picus.core.chat.adapter.in;

import com.picus.core.chat.adapter.in.web.data.request.CreateChatRoomRequest;
import com.picus.core.chat.adapter.in.web.mapper.CreateChatRoomWebMapper;
import com.picus.core.chat.application.port.in.CreateChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class CreateChatRoomController {

    private final CreateChatRoomUseCase useCase;

    private final CreateChatRoomWebMapper webMapper;

    @PostMapping
    public BaseResponse<String> create(@RequestBody @Valid CreateChatRoomRequest request, @CurrentUser String userNo) {
        String chatRoomNo = useCase.create(webMapper.toCommand(userNo, request)); // 채팅방 생성

        return BaseResponse.onSuccess(chatRoomNo);
    }
}
