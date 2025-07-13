package com.picus.core.expert.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Project {
    private String projectName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
