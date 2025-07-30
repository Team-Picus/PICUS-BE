package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.LoadExpertBasicInfoResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoResponse.ProjectResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoResponse.SkillResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoResponse.StudioResponse;
import com.picus.core.expert.application.port.in.result.ExpertBasicInfoResult;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadExpertWebMapper {

    public LoadExpertBasicInfoResponse toBasicInfoResponse(ExpertBasicInfoResult appResponse) {
        return LoadExpertBasicInfoResponse.builder()
                .expertNo(appResponse.expertNo())
                .activityDuration(appResponse.activityDuration())
                .activityCount(appResponse.activityCount())
                .lastActivityAt(appResponse.lastActivityAt())
                .intro(appResponse.intro())
                .backgroundImageUrl(appResponse.backgroundImageUrl())
                .nickname(appResponse.nickname())
                .profileImageUrl(appResponse.profileImageUrl())
                .links(appResponse.links())
                .build();
    }

    public LoadExpertDetailInfoResponse toDetailInfoResponse(Expert expert) {

        return LoadExpertDetailInfoResponse.builder()
                .activityCareer(expert.getActivityCareer())
                .projects(toProjectResponse(expert.getProjects()))
                .skills(toSkillResponse(expert.getSkills()))
                .activityAreas(expert.getActivityAreas())
                .studio(toStudioResponse(expert.getStudio()))
                .build();
    }

    private List<ProjectResponse> toProjectResponse(List<Project> projects) {
        return projects.stream()
                .map(project -> ProjectResponse.builder()
                        .projectNo(project.getProjectNo())
                        .projectName(project.getProjectName())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .build()
                ).toList();
    }

    private List<SkillResponse> toSkillResponse(List<Skill> skills) {
        return skills.stream()
                .map(skill -> SkillResponse.builder()
                        .skillNo(skill.getSkillNo())
                        .skillType(skill.getSkillType())
                        .content(skill.getContent())
                        .build()
                ).toList();
    }

    private StudioResponse toStudioResponse(Studio studio) {
        return StudioResponse.builder()
                .studioNo(studio.getStudioNo())
                .studioName(studio.getStudioName())
                .employeesCount(studio.getEmployeesCount())
                .businessHours(studio.getBusinessHours())
                .address(studio.getAddress())
                .build();
    }
}
