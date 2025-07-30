package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.LoadExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoWebResponse.ProjectWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoWebResponse.SkillWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoWebResponse.StudioWebResponse;
import com.picus.core.expert.application.port.in.response.ExpertBasicInfoResult;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadExpertWebMapper {

    public LoadExpertBasicInfoWebResponse toBasicInfo(ExpertBasicInfoResult appResponse) {
        return LoadExpertBasicInfoWebResponse.builder()
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

    public LoadExpertDetailInfoWebResponse toDetailInfo(Expert expert) {

        return LoadExpertDetailInfoWebResponse.builder()
                .activityCareer(expert.getActivityCareer())
                .projects(toProjectWebResponse(expert.getProjects()))
                .skills(toSkillWebResponse(expert.getSkills()))
                .activityAreas(expert.getActivityAreas())
                .studio(toStudioWebResponse(expert.getStudio()))
                .build();
    }

    private List<ProjectWebResponse> toProjectWebResponse(List<Project> projects) {
        return projects.stream()
                .map(project -> ProjectWebResponse.builder()
                        .projectNo(project.getProjectNo())
                        .projectName(project.getProjectName())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .build()
                ).toList();
    }

    private List<SkillWebResponse> toSkillWebResponse(List<Skill> skills) {
        return skills.stream()
                .map(skill -> SkillWebResponse.builder()
                        .skillNo(skill.getSkillNo())
                        .skillType(skill.getSkillType())
                        .content(skill.getContent())
                        .build()
                ).toList();
    }

    private StudioWebResponse toStudioWebResponse(Studio studio) {
        return StudioWebResponse.builder()
                .studioNo(studio.getStudioNo())
                .studioName(studio.getStudioName())
                .employeesCount(studio.getEmployeesCount())
                .businessHours(studio.getBusinessHours())
                .address(studio.getAddress())
                .build();
    }
}
