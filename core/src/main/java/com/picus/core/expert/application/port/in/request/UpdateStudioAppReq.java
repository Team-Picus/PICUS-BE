package com.picus.core.expert.application.port.in.request;

import lombok.Builder;

@Builder
public record UpdateStudioAppReq(
        String studioNo,
        String studioName,
        Integer employeesCount,
        String businessHours,
        String address,
        ChangeStatus changeStatus
) {
}
