package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class Expert {
    private String expertNo;

    private String backgroundImageKey;

    private String intro;

    private String activityCareer;

    private List<ActivityArea> activityAreas;

    private String activityDuration;

    private Integer activityCount;

    private LocalDateTime lastActivityAt;

    private List<Portfolio> portfolios;

    private ApprovalStatus approvalStatus;

    private Studio studio;

    private List<Skill> skills;

    private List<Project> projects;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public void bindExpertNo(String expertNo) {
        this.expertNo = expertNo;
    }
}
