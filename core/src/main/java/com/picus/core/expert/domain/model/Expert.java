package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Getter
public class Expert {
    private String expertNo;

    private final String backgroundImageKey;

    private final String intro;

    private final String activityCareer;

    private final List<ActivityArea> activityAreas;

    private final String activityDuration;

    private final Integer activityCount;

    private final LocalDateTime lastActivityAt;

    private final List<Portfolio> portfolios;

    private final ApprovalStatus approvalStatus;

    private final Studio studio;

    private final List<Skill> skills;

    private final List<Project> projects;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final LocalDateTime deletedAt;

    @Builder
    public Expert(String expertNo,
                  String backgroundImageKey,
                  String intro,
                  String activityCareer,
                  List<ActivityArea> activityAreas,
                  Integer activityCount,
                  LocalDateTime lastActivityAt,
                  List<Portfolio> portfolios,
                  ApprovalStatus approvalStatus,
                  Studio studio,
                  List<Skill> skills,
                  List<Project> projects,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt,
                  LocalDateTime deletedAt) {

        this.expertNo = expertNo;
        this.backgroundImageKey = backgroundImageKey;
        this.intro = intro;
        this.activityCareer = activityCareer;
        this.activityAreas = activityAreas;
        this.activityCount = activityCount;
        this.lastActivityAt = lastActivityAt;
        this.portfolios = portfolios;
        this.approvalStatus = approvalStatus;
        this.studio = studio;
        this.skills = skills;
        this.projects = projects;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.activityDuration = calculateActivityDuration(LocalDate.now());
    }
    public void bindExpertNo(String expertNo) {
        this.expertNo = expertNo;
    }

    public String calculateActivityDuration(LocalDate now) {
        if (this.createdAt == null) return null;

        LocalDate createDate = createdAt.toLocalDate();
        Period period = Period.between(createDate, now);

        String activityDuration;
        if (period.getYears() >= 1) {
            activityDuration = period.getYears() + "년";
        } else if (period.getMonths() >= 1) {
            activityDuration = period.getMonths() + "개월";
        } else {
            activityDuration = period.getDays() + "일";
        }

        return activityDuration;
    }
}
