package com.picus.core.expert.infra.adapter.in;

import com.picus.core.expert.application.port.in.RequestApprovalRequest;
import com.picus.core.expert.application.port.in.RequestApprovalResponse;
import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.infra.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.infra.adapter.in.web.data.response.RequestApprovalWebResponse;
import com.picus.core.expert.infra.adapter.in.web.mapper.RequestApprovalRequestWebMapper;
import com.picus.core.expert.infra.adapter.in.web.mapper.RequestApprovalResponseWebMapper;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RequestApprovalController {

    private final RequestApprovalUseCase requestApprovalUseCase;
    private final RequestApprovalRequestWebMapper requestWebMapper;
    private final RequestApprovalResponseWebMapper responseWebMapper;

    @PostMapping("api/v1/experts/approval-requests")
    public BaseResponse<RequestApprovalWebResponse> requestApproval(@RequestBody RequestApprovalWebRequest webRequest) {

        RequestApprovalRequest applicationRequest = requestWebMapper.toApplicationRequest(webRequest); // 웹 요청 -> 어플리케이션 요청
        RequestApprovalResponse applicationResponse = requestApprovalUseCase.requestApproval(applicationRequest); // 유스케이스 호출
        RequestApprovalWebResponse webResponse = responseWebMapper.toWebResponse(applicationResponse); // 어플리케이션 응답 -> 웹 응답
        return BaseResponse.onSuccess(webResponse);
    }
}
