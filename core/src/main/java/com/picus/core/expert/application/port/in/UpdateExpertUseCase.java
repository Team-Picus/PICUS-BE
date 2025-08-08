package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.command.UpdateExpertBasicInfoCommand;
import com.picus.core.expert.application.port.in.command.UpdateExpertDetailInfoCommand;

/**
 * 전문가의 정보를 수정한다.
 */
public interface UpdateExpertUseCase {

    void updateExpertBasicInfo(UpdateExpertBasicInfoCommand basicInfoRequest);

    void updateExpertDetailInfo(UpdateExpertDetailInfoCommand detailInfoRequest);
}
