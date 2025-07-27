package com.picus.core.expert.adapter.in;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.GetExpertWebMapper;
import com.picus.core.expert.application.port.in.LoadExpertUseCase;
import com.picus.core.expert.application.port.in.response.ExpertBasicInfoQueryAppResp;
import com.picus.core.expert.domain.Expert;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadExpertController {

    private final LoadExpertUseCase loadExpertUseCase;
    private final GetExpertWebMapper getExpertWebMapper;

    @GetMapping("/api/v1/experts/{expert_no}/basic_info")
    public BaseResponse<GetExpertBasicInfoWebResponse> getExpertBasicInfo(@PathVariable("expert_no") String expertNo) {

        ExpertBasicInfoQueryAppResp appResponse = loadExpertUseCase.getExpertBasicInfo(expertNo);
        return BaseResponse.onSuccess(getExpertWebMapper.toBasicInfo(appResponse));
    }

    @GetMapping("/api/v1/experts/{expert_no}/detail_info")
    public BaseResponse<GetExpertDetailInfoWebResponse> getExpertDetailInfo(@PathVariable("expert_no") String expertNo) {
        Expert expert = loadExpertUseCase.getExpertDetailInfo(expertNo);
        return BaseResponse.onSuccess(getExpertWebMapper.toDetailInfo(expert));
    }


}
