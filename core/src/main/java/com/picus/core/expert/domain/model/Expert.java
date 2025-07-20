package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
public class Expert {
    private String expertNo;

    private String backgroundImageKey;

    private final String backgroundImageUrl;

    private String intro;

    private String activityCareer;

    private List<String> activityAreas;

    private final String activityDuration;

    private final Integer activityCount;

    private final LocalDateTime lastActivityAt;

    private List<Portfolio> portfolios;

    private ApprovalStatus approvalStatus;

    private Studio studio;

    private List<Skill> skills;

    private List<Project> projects;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final LocalDateTime deletedAt;

    @Builder
    public Expert(String expertNo,
                  String backgroundImageKey, String backgroundImageUrl,
                  String intro,
                  String activityCareer,
                  List<String> activityAreas,
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
        this.backgroundImageUrl = backgroundImageUrl;
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

        long totalYears = ChronoUnit.YEARS.between(createDate, now);
        long totalMonths = ChronoUnit.MONTHS.between(createDate, now);
        long totalDays = ChronoUnit.DAYS.between(createDate, now);

        String activityDuration;
        if (totalYears >= 1) {
            activityDuration = totalYears + "년";
        } else if (totalMonths >= 1) {
            activityDuration = totalMonths + "개월";
        } else {
            activityDuration = totalDays + "일";
        }
        return activityDuration;
    }

    public void approveApprovalRequest() {
        this.approvalStatus = ApprovalStatus.APPROVAL;
    }
    public void rejectApprovalRequest() {
        this.approvalStatus = ApprovalStatus.REJECT;
    }

    public void updateBasicInfo(String backgroundImageKey, String link, String intro) {
        if (backgroundImageKey != null) {
            this.backgroundImageKey = backgroundImageKey;
        }

        if (link != null) {
            if (this.portfolios == null) {
                this.portfolios = new ArrayList<>();
            }
            this.portfolios.add(Portfolio.builder().link(link).build());
        }

        if (intro != null) {
            this.intro = intro;
        }
    }

    public void updateDetailInfo(
            String activityCareer, List<String> activityAreas, List<Project> projects,
            List<Skill> skills, Studio studio) {
        if(activityCareer != null)
            this.activityCareer = activityCareer;

        if(activityAreas != null)
            this.activityAreas = activityAreas;

        if(projects != null)
            this.projects = projects;

        if(skills != null)
            this.skills = skills;

        if(studio != null)
            this.studio = studio;

    }
}
