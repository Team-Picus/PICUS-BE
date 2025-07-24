package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.response.ExpertBasicInfoQueryAppResp;
import com.picus.core.expert.domain.model.Expert;

public interface ExpertInfoQuery {

    Expert getExpertDetailInfo(String expertNo);

    ExpertBasicInfoQueryAppResp getExpertBasicInfo(String expertNo);

}
