package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.response.GetExpertBasicInfoAppResponse;
import com.picus.core.expert.domain.model.Expert;

public interface GetExpertInfoQuery {

    Expert getExpertDetailInfo(String expertNo);

    GetExpertBasicInfoAppResponse getExpertBasicInfo(String expertNo);

}
