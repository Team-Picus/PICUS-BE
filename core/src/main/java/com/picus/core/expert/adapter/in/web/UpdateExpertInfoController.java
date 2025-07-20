package com.picus.core.expert.adapter.in.web;


import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest;
import com.picus.core.expert.adapter.in.web.mapper.UpdateExpertWebMapper;
import com.picus.core.expert.application.port.in.ExpertInfoCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateExpertInfoController {
    private final ExpertInfoCommand expertInfoCommand;
    private final UpdateExpertWebMapper updateExpertWebMapper;

    @PatchMapping("/api/v1/experts/basic_info")
    public BaseResponse<Void> updateExpertBasicInfo(@RequestBody UpdateExpertBasicInfoWebRequest webRequest,
                                                    @CurrentUser String userNo) {
        // 유스케이스 호출
        expertInfoCommand.updateExpertBasicInfo(updateExpertWebMapper.toBasicInfoAppRequest(webRequest, userNo));
        return BaseResponse.onSuccess();
    }

    @PatchMapping("/api/v1/experts/detail_info")
    public BaseResponse<Void> updateExpertDetailInfo(@RequestBody @Valid UpdateExpertDetailInfoWebRequest webRequest,
                                                     @CurrentUser String userNo) {
        // 유스케이스 호출
        expertInfoCommand.updateExpertDetailInfo(updateExpertWebMapper.toDetailInfoAppRequest(webRequest, userNo));
        return BaseResponse.onSuccess();
    }
}
