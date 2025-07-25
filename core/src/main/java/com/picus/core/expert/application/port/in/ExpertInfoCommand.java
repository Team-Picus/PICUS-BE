package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.command.UpdateExpertBasicInfoAppRequest;
import com.picus.core.expert.application.port.in.command.UpdateExpertDetailInfoAppRequest;

/**
 * 전문가의 정보를 수정한다.
 */
public interface ExpertInfoCommand {

    void updateExpertBasicInfo(UpdateExpertBasicInfoAppRequest basicInfoRequest);

    void updateExpertDetailInfo(UpdateExpertDetailInfoAppRequest detailInfoRequest);
}
