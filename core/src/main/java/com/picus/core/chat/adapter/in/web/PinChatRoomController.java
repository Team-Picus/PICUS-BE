package com.picus.core.chat.adapter.in.web;

import com.picus.core.chat.adapter.in.web.data.request.PinChatRoomRequest;
import com.picus.core.chat.adapter.in.web.mapper.PinChatRoomWebMapper;
import com.picus.core.chat.application.port.in.PinChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.PinChatRoomCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class PinChatRoomController {

    private final PinChatRoomUseCase useCase;

    private final PinChatRoomWebMapper webMapper;

    @PostMapping("/pin")
    public BaseResponse<?> pin(@RequestBody @Valid PinChatRoomRequest request, @CurrentUser String userNo) {
        PinChatRoomCommand command = webMapper.toCommand(request, userNo);
        useCase.pin(command); // 유스케이스 호출

        return BaseResponse.onSuccess();
    }
}