package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.adapter.in.web.mapper.RequestApprovalWebMapper;
import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.adapter.in.web.data.response.RequestApprovalWebResponse;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RequestApprovalController {

    private final RequestApprovalUseCase requestApprovalUseCase;
    private final RequestApprovalWebMapper webMapper;

    @PostMapping("api/v1/experts/approval-requests")
    public BaseResponse<RequestApprovalWebResponse> requestApproval(@RequestBody RequestApprovalWebRequest webRequest) {

        Expert request = webMapper.toDomain(webRequest); // 웹 요청 -> 도메인
        Expert response = requestApprovalUseCase.requestApproval(request); // 유스케이스 호출
        RequestApprovalWebResponse webResponse = webMapper.toWebResponse(response); // 도메인 -> 웹 응답
        return BaseResponse.onSuccess(webResponse);
    }
}
