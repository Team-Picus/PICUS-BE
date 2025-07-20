package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.ProjectWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.SkillWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.StudioWebRequest;
import com.picus.core.expert.application.port.in.command.UpdateExpertBasicInfoAppRequest;
import com.picus.core.expert.application.port.in.command.UpdateExpertDetailInfoAppRequest;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.SkillType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateExpertWebMapper {

    public UpdateExpertBasicInfoAppRequest toBasicInfoAppRequest(UpdateExpertBasicInfoWebRequest webRequest, String currentUserNo) {
        return UpdateExpertBasicInfoAppRequest.builder()
                .currentUserNo(currentUserNo)
                .profileImageFileKey(webRequest.profileImageFileKey())
                .backgroundImageFileKey(webRequest.backgroundImageFileKey())
                .nickname(webRequest.nickname())
                .link(webRequest.link())
                .intro(webRequest.intro())
                .build();
    }

    public UpdateExpertDetailInfoAppRequest toDetailInfoAppRequest(UpdateExpertDetailInfoWebRequest webRequest, String currentUserNo) {

        return UpdateExpertDetailInfoAppRequest.builder()
                .currentUserNo(currentUserNo)
                .activityCareer(webRequest.activityCareer())
                .activityAreas(webRequest.activityAreas())
                .projects(toProjectDomain(webRequest.projects()))
                .skills(toSkillDomain(webRequest.skills()))
                .studio(toStudioDomain(webRequest.studio()))
                .build();
    }

    private List<Project> toProjectDomain(List<ProjectWebRequest> projectWebRequests) {
        return projectWebRequests.stream()
                .map(webRequest -> Project.builder()
                        .projectNo(webRequest.projectNo())
                        .projectName(webRequest.projectName())
                        .startDate(webRequest.startDate())
                        .endDate(webRequest.endDate())
                        .build())
                .toList();
    }

    private List<Skill> toSkillDomain(List<SkillWebRequest> skillWebRequests) {
        return skillWebRequests.stream()
                .map(webRequest -> Skill.builder()
                        .skillNo(webRequest.skillNo())
                        .skillType(SkillType.valueOf(webRequest.skillType()))
                        .content(webRequest.content())
                        .build())
                .toList();
    }

    private Studio toStudioDomain(StudioWebRequest studioWebRequest) {
        return Studio.builder()
                .studioNo(studioWebRequest.studioNo())
                .studioName(studioWebRequest.studioName())
                .employeesCount(studioWebRequest.employeesCount())
                .businessHours(studioWebRequest.businessHours())
                .address(studioWebRequest.address())
                .build();
    }
}
