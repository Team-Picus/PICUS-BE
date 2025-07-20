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
import java.util.Optional;

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
        this.skills = skills == null ? new ArrayList<>() : skills;
        this.projects = projects == null ? new ArrayList<>() : projects;
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
        // activityCareer 업데이트
        if (activityCareer != null)
            this.activityCareer = activityCareer;

        // activityAreas 업데이트 (덮어 씌움)
        if (activityAreas != null)
            this.activityAreas = activityAreas;

        // projects 업데이트
        if (projects != null) {
            updateProjects(projects);
        }

        // skills 업데이트
        if (skills != null) {
            updateSkills(skills);
        }

        // studio 업데이트
        if (studio != null) {
            updateStudio(studio);
        }
    }

    private void updateProjects(List<Project> projects) {
        // 이뮤터블 리스트 방어 (불변 객체가 들어와도 안전하게)
        if (!(this.projects instanceof ArrayList)) {
            this.projects = new ArrayList<>(this.projects);
        }
        for (Project incoming : projects) {
            if (incoming.getProjectNo() != null) {
                // 수정: 기존 projectNo 찾기
                Optional<Project> target = this.projects.stream()
                        .filter(p -> p.getProjectNo().equals(incoming.getProjectNo()))
                        .findFirst();

                target.ifPresent(existing -> {
                    existing.updateProject(
                            incoming.getProjectName(),
                            incoming.getStartDate(),
                            incoming.getEndDate()
                    );
                });
            } else {
                // 추가
                this.projects.add(incoming);
            }
        }
    }

    private void updateSkills(List<Skill> skills) {
        if (!(this.skills instanceof ArrayList)) {
            this.skills = new ArrayList<>(this.skills);
        }
        for (Skill incoming : skills) {
            if (incoming.getSkillNo() != null) {
                // 수정: 기존 skillNo 찾기
                Optional<Skill> target = this.skills.stream()
                        .filter(s -> s.getSkillNo().equals(incoming.getSkillNo()))
                        .findFirst();

                target.ifPresent(existing -> {
                    existing.updateSkill(incoming.getSkillType(), incoming.getContent());
                });
            } else {
                // 추가
                this.skills.add(incoming);
            }
        }
    }

    private void updateStudio(Studio studio) {
        if (this.studio == null) {
            this.studio = Studio.builder()
                    .studioName(studio.getStudioName())
                    .employeesCount(studio.getEmployeesCount())
                    .address(studio.getAddress())
                    .build();
        } else {
            this.studio.updateStudio(
                    studio.getStudioName(),
                    studio.getEmployeesCount(),
                    studio.getBusinessHours(),
                    studio.getAddress());
        }
    }
}
