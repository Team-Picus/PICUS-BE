package com.picus.core.expert.adapter.in;

import com.picus.core.expert.adapter.in.web.data.response.LoadExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.GetExpertWebMapper;
import com.picus.core.expert.application.port.in.LoadExpertUseCase;
import com.picus.core.expert.application.port.in.response.ExpertBasicInfoResult;
import com.picus.core.expert.domain.Expert;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class LoadExpertController {

    private final LoadExpertUseCase loadExpertUseCase;
    private final GetExpertWebMapper getExpertWebMapper;

    @GetMapping("/{expert_no}/basic_info")
    public BaseResponse<LoadExpertBasicInfoWebResponse> getExpertBasicInfo(@PathVariable("expert_no") String expertNo) {

        ExpertBasicInfoResult appResponse = loadExpertUseCase.getExpertBasicInfo(expertNo);
        return BaseResponse.onSuccess(getExpertWebMapper.toBasicInfo(appResponse));
    }

    @GetMapping("/{expert_no}/detail_info")
    public BaseResponse<LoadExpertDetailInfoWebResponse> getExpertDetailInfo(@PathVariable("expert_no") String expertNo) {
        Expert expert = loadExpertUseCase.getExpertDetailInfo(expertNo);
        return BaseResponse.onSuccess(getExpertWebMapper.toDetailInfo(expert));
    }


}
