package com.picus.core.expert.application.port.in.command;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
public record ProjectCommand(
        String projectNo,
        String projectName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        ChangeStatus changeStatus
) {
}
