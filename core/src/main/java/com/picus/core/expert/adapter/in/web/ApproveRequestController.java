package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.application.port.in.ApproveRequestUseCase;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 전문가 승인 요청 수락 API
 */
@RestController
@RequiredArgsConstructor
public class ApproveRequestController {

    private final ApproveRequestUseCase approveRequestUseCase;

    @PatchMapping("/api/v1/experts/{expert_no}/approval-requests/approval")
    public BaseResponse<Void> approveRequest(@PathVariable(value = "expert_no") String expertNo) {
        approveRequestUseCase.approveRequest(expertNo);
        return BaseResponse.onSuccess();
    }

}
