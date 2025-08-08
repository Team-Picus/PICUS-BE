package com.picus.core.expert.adapter.in;


import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest;
import com.picus.core.expert.adapter.in.web.mapper.UpdateExpertWebMapper;
import com.picus.core.expert.application.port.in.UpdateExpertUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class UpdateExpertController {
    private final UpdateExpertUseCase updateExpertUseCase;
    private final UpdateExpertWebMapper updateExpertWebMapper;

    @PatchMapping("/basic_info")
    public BaseResponse<Void> updateExpertBasicInfo(@RequestBody UpdateExpertBasicInfoRequest webRequest,
                                                    @CurrentUser String userNo) {
        // 유스케이스 호출
        updateExpertUseCase.updateExpertBasicInfo(updateExpertWebMapper.toBasicInfoCommand(webRequest, userNo));
        return BaseResponse.onSuccess();
    }

    @PatchMapping("/detail_info")
    public BaseResponse<Void> updateExpertDetailInfo(@RequestBody @Valid UpdateExpertDetailInfoRequest webRequest,
                                                     @CurrentUser String userNo) {
        // 유스케이스 호출
        updateExpertUseCase.updateExpertDetailInfo(updateExpertWebMapper.toDetailInfoCommand(webRequest, userNo));
        return BaseResponse.onSuccess();
    }
}
