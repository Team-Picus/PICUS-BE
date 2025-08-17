package com.picus.core.chat.adapter.in;

import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.adapter.in.web.mapper.ExitChatRoomWebMapper;
import com.picus.core.chat.application.port.in.ExitChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
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
public class ExitChatRoomController {

    private final ExitChatRoomUseCase useCase;

    private final ExitChatRoomWebMapper webMapper;

    @PostMapping("/exit")
    public BaseResponse<Void> exit(@RequestBody @Valid ExitChatRoomRequest request, @CurrentUser String userNo) {
        ExitChatRoomCommand command = webMapper.toCommand(request, userNo); // Command로 매핑
        useCase.exit(command); // 유스케이스 호출
        return BaseResponse.onSuccess();
    }
}
