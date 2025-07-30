package com.picus.core.expert.application.port.in.request;

import lombok.Builder;

@Builder
public record UpdateStudioCommand(
        String studioNo,
        String studioName,
        Integer employeesCount,
        String businessHours,
        String address,
        ChangeStatus changeStatus
) {
}
