package com.picus.core.expert.adapter.in;

import com.picus.core.expert.adapter.in.web.mapper.RequestApprovalWebMapper;
import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.application.port.in.request.RequestApprovalCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/experts")
public class RequestApprovalController {

    private final RequestApprovalUseCase requestApprovalUseCase;
    private final RequestApprovalWebMapper webMapper;

    @PostMapping("/approval-requests")
    public BaseResponse<Void> requestApproval(
            @Valid @RequestBody RequestApprovalWebRequest webRequest, @CurrentUser String userNo) {

        RequestApprovalCommand command = webMapper.toCommand(webRequest, userNo);// 웹 요청 -> command
        requestApprovalUseCase.requestApproval(command); // 유스케이스 호출
        return BaseResponse.onSuccess();
    }
}
