package com.picus.core.expert.adapter.in;

import com.picus.core.expert.application.port.in.RejectRequestUseCase;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 전문가 승인 요청 거절 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class RejectRequestController {

    private final RejectRequestUseCase rejectRequestUseCase;

    @PatchMapping("/{expert_no}/approval-requests/rejection")
    public BaseResponse<Void> rejectRequest(@PathVariable(value = "expert_no") String expertNo) {
        rejectRequestUseCase.rejectRequest(expertNo);
        return BaseResponse.onSuccess();
    }

}
