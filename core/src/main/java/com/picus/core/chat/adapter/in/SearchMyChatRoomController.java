package com.picus.core.chat.adapter.in;

import com.picus.core.chat.adapter.in.web.data.response.SearchMyChatRoomResponse;
import com.picus.core.chat.adapter.in.web.mapper.SearchMyChatRoomWebMapper;
import com.picus.core.chat.application.port.in.SearchMyChatRoomUseCase;
import com.picus.core.chat.application.port.in.result.SearchMyChatRoomResult;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class SearchMyChatRoomController {

    private final SearchMyChatRoomUseCase useCase;
    private final SearchMyChatRoomWebMapper webMapper;

    @GetMapping("/search")
    public BaseResponse<SearchMyChatRoomResponse> search(@RequestParam("keyword") String keyword, @CurrentUser String userNo) {
        List<SearchMyChatRoomResult> results = useCase.search(userNo, keyword); // 유스케이스 호출
        return BaseResponse.onSuccess(webMapper.toResponse(results));
    }
}
