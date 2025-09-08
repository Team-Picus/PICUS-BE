package com.picus.core.chat.adapter.in;

import com.picus.core.chat.adapter.in.web.data.response.LoadMyChatRoomListResponse;
import com.picus.core.chat.adapter.in.web.mapper.LoadMyChatRoomListWebMapper;
import com.picus.core.chat.application.port.in.LoadMyChatRoomListUseCase;
import com.picus.core.chat.application.port.in.result.LoadMyChatRoomListResult;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class LoadMyChatRoomListController {

    private final LoadMyChatRoomListUseCase useCase;
    private final LoadMyChatRoomListWebMapper webMapper;

    @GetMapping
    public BaseResponse<LoadMyChatRoomListResponse> load(@CurrentUser String userNo) {
        List<LoadMyChatRoomListResult> results = useCase.load(userNo);
        return BaseResponse.onSuccess(webMapper.toResponse(results));
    }
}
