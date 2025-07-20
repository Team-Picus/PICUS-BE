package com.picus.core.expert.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
public class Project {
    private String projectNo;
    private String projectName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
