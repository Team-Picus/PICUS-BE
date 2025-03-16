package com.picus.core.domain.studio.application.converter;

import com.picus.core.domain.studio.application.dto.response.StudioSummaryDto;
import com.picus.core.domain.studio.domain.entity.Studio;

public abstract class StudioConverter {

    public static StudioSummaryDto toStudioSummaryDto(Studio studio) {
        return new StudioSummaryDto(
                studio.getId(),              // studioNo
                studio.getName(),
                studio.getBackgroundImgUrl(),
                studio.getAddress(),
                studio.getRecentActiveAt(),
                studio.getExpertNo(),
                studio.getApprovalStatus()
        );
    }


    // TODO redis에서 Stduio 관련 통계 정보를 가져와서 StudioDetailDto에 추가해야 함

}
