package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse.ProjectWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse.SkillWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse.StudioWebResponse;
import com.picus.core.expert.application.port.in.response.GetExpertBasicInfoAppResponse;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetExpertWebMapper {

    public GetExpertBasicInfoWebResponse toBasicInfo(GetExpertBasicInfoAppResponse appResponse) {
        return GetExpertBasicInfoWebResponse.builder()
                .expertNo(appResponse.expertNo())
                .activityDuration(appResponse.activityDuration())
                .activityCount(appResponse.activityCount())
                .lastActivityAt(appResponse.lastActivityAt())
                .intro(appResponse.intro())
                .backgroundImageUrl(appResponse.backgroundImageUrl())
                .nickname(appResponse.nickname())
                .profileImageUrl(appResponse.profileImageUrl())
                .build();
    }

    public GetExpertDetailInfoWebResponse toDetailInfo(Expert expert) {

        return GetExpertDetailInfoWebResponse.builder()
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
                        .projectName(project.getProjectName())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .build()
                ).toList();
    }

    private List<SkillWebResponse> toSkillWebResponse(List<Skill> skills) {
        return skills.stream()
                .map(skill -> SkillWebResponse.builder()
                        .skillType(skill.getSkillType())
                        .content(skill.getContent())
                        .build()
                ).toList();
    }

    private StudioWebResponse toStudioWebResponse(Studio studio) {
        return StudioWebResponse.builder()
                .studioName(studio.getStudioName())
                .employeesCount(studio.getEmployeesCount())
                .businessHours(studio.getBusinessHours())
                .address(studio.getAddress())
                .build();
    }
}
