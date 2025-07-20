package com.picus.core.expert.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
public class Project {
    private final String projectNo;
    private String projectName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public void updateProject(String projectName, LocalDateTime startDate, LocalDateTime endDate) {
        if(projectName != null)
            this.projectName = projectName;
        if(startDate != null)
            this.startDate = startDate;
        if(endDate != null)
            this.endDate = endDate;
    }
}
