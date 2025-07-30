package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.response.ExpertBasicInfoResult;
import com.picus.core.expert.domain.Expert;

public interface LoadExpertUseCase {

    Expert getExpertDetailInfo(String expertNo);

    ExpertBasicInfoResult getExpertBasicInfo(String expertNo);

}
