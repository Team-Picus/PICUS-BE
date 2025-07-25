package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.request.WritePostWebReq;
import com.picus.core.post.adapter.in.web.mapper.WritePostWebMapper;
import com.picus.core.post.application.port.in.PostInfoCommand;
import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostInfoCommandController {

    private final PostInfoCommand postInfoCommand;

    private final WritePostWebMapper webMapper;

    @PostMapping("/api/v1/posts")
    public BaseResponse<Void> writePost(@RequestBody @Valid WritePostWebReq webReq, @CurrentUser String userNo) {
        WritePostAppReq appReq = webMapper.toAppReq(webReq, userNo);
        postInfoCommand.writePost(appReq);
        return BaseResponse.onSuccess();
    }
}
