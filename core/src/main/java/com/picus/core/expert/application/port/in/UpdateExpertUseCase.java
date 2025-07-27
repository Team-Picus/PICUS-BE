package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.request.UpdateExpertBasicInfoAppReq;
import com.picus.core.expert.application.port.in.request.UpdateExpertDetailInfoAppReq;

/**
 * 전문가의 정보를 수정한다.
 */
public interface UpdateExpertUseCase {

    void updateExpertBasicInfo(UpdateExpertBasicInfoAppReq basicInfoRequest);

    void updateExpertDetailInfo(UpdateExpertDetailInfoAppReq detailInfoRequest);
}
