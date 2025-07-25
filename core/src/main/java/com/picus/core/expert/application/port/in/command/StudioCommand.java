package com.picus.core.expert.application.port.in.command;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
public record StudioCommand(
        String studioNo,
        String studioName,
        Integer employeesCount,
        String businessHours,
        String address,
        ChangeStatus changeStatus
) {
}
