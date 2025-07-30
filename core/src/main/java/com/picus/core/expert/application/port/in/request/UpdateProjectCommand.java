package com.picus.core.expert.application.port.in.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateProjectCommand(
        String projectNo,
        String projectName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        ChangeStatus changeStatus
) {
}
