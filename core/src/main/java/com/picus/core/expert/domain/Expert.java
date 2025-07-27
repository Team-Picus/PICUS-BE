package com.picus.core.expert.domain;

import com.picus.core.expert.domain.vo.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private Integer activityCount;

    private LocalDateTime lastActivityAt;

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

    public void updateBasicInfo(String backgroundImageKey, List<String> links, String intro) {
        if (backgroundImageKey != null) {
            this.backgroundImageKey = backgroundImageKey;
        }

        if (links != null) {
            this.portfolios = links.stream()
                    .map(link -> Portfolio.builder().link(link).build())
                    .toList();
        }

        if (intro != null) {
            this.intro = intro;
        }
    }

    public void updateDetailInfo(
            String activityCareer, List<String> activityAreas) {
        // activityCareer 업데이트
        if (activityCareer != null)
            this.activityCareer = activityCareer;

        // activityAreas 업데이트 (덮어 씌움)
        if (activityAreas != null)
            this.activityAreas = activityAreas;
    }

    public void updateLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    // 프로젝트 변경
    public void addProject(Project newProject) {
        // 이뮤터블 리스트 방어
        if (!(this.projects instanceof ArrayList)) {
            this.projects = new ArrayList<>(this.projects);
        }
        this.projects.add(newProject);
    }

    public void updateProject(Project updatedProject) {
        // 이뮤터블 리스트 방어
        if (!(this.projects instanceof ArrayList)) {
            this.projects = new ArrayList<>(this.projects);
        }
        if (updatedProject.getProjectNo() != null) {
            for (Project project : this.projects) {
                if (updatedProject.getProjectNo().equals(project.getProjectNo())) {
                    project.updateProject(
                            updatedProject.getProjectName(), updatedProject.getStartDate(), updatedProject.getEndDate()
                    );
                    break;
                }
            }
        }
    }

    public void deleteProject(String projectNo) {
        // 이뮤터블 리스트 방어
        if (!(this.projects instanceof ArrayList)) {
            this.projects = new ArrayList<>(this.projects);
        }
        if (projectNo != null) {
            this.projects.removeIf(project ->
                    projectNo.equals(project.getProjectNo()));
        }
    }

    // Skill 변경
    public void addSkill(Skill newSkill) {
        // 이뮤터블 리스트 방어
        if (!(this.skills instanceof ArrayList)) {
            this.skills = new ArrayList<>(this.skills);
        }
        this.skills.add(newSkill);
    }

    public void updateSkill(Skill updatedSkill) {
        if (!(this.skills instanceof ArrayList)) {
            this.skills = new ArrayList<>(this.skills);
        }
        if (updatedSkill.getSkillNo() != null) {
            for (Skill skill : this.skills) {
                if (updatedSkill.getSkillNo().equals(skill.getSkillNo())) {
                    skill.updateSkill(updatedSkill.getSkillType(), updatedSkill.getContent());
                    break;
                }
            }
        }
    }

    public void deleteSkill(String skillNo) {
        // 이뮤터블 리스트 방어
        if (!(this.skills instanceof ArrayList)) {
            this.skills = new ArrayList<>(this.skills);
        }
        if (skillNo != null) {
            this.skills.removeIf(skill ->
                    skillNo.equals(skill.getSkillNo()));
        }
    }

    // Studio 변경
    public void addStudio(Studio newStudio) {
        if (newStudio != null)
            this.studio = newStudio;
    }
    public void updateStudio(Studio updatedStudio) {
        if (updatedStudio != null && this.studio != null) {
            if (Objects.equals(updatedStudio.getStudioNo(), this.studio.getStudioNo())) {
                this.studio.updateStudio(
                        updatedStudio.getStudioName(),
                        updatedStudio.getEmployeesCount(),
                        updatedStudio.getBusinessHours(),
                        updatedStudio.getAddress());
            }
        }
    }
    public void deleteStudio() {
        this.studio = null;
    }

    public void decreaseActivityCount() {
        this.activityCount--;
    }
}
