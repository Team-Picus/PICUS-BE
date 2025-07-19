package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.command.ExpertBasicInfoCommandRequest;
import com.picus.core.expert.application.port.in.command.ExpertDetailInfoCommandRequest;

/**
 * 전문가의 정보를 수정한다.
 */
public interface ExpertInfoCommand {

    void updateExpertBasicInfo(ExpertBasicInfoCommandRequest basicInfoRequest);

    void updateExpertDetailInfo(ExpertDetailInfoCommandRequest detailInfoRequest);
}
