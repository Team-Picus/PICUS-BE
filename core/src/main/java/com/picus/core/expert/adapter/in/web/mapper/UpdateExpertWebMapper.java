package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.ProjectWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.SkillWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.StudioWebRequest;
import com.picus.core.expert.application.port.in.command.*;
import com.picus.core.expert.domain.vo.SkillType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateExpertWebMapper {

    public UpdateExpertBasicInfoCommand toBasicInfoCommand(UpdateExpertBasicInfoRequest webRequest, String currentUserNo) {
        return UpdateExpertBasicInfoCommand.builder()
                .currentUserNo(currentUserNo)
                .profileImageFileKey(webRequest.profileImageFileKey())
                .backgroundImageFileKey(webRequest.backgroundImageFileKey())
                .nickname(webRequest.nickname())
                .link(webRequest.link())
                .intro(webRequest.intro())
                .build();
    }

    public UpdateExpertDetailInfoCommand toDetailInfoCommand(UpdateExpertDetailInfoRequest webRequest, String currentUserNo) {

        return UpdateExpertDetailInfoCommand.builder()
                .currentUserNo(currentUserNo)
                .activityCareer(webRequest.activityCareer())
                .activityAreas(webRequest.activityAreas())
                .projects(toProjectCommand(webRequest.projects()))
                .skills(toSkillCommand(webRequest.skills()))
                .studio(toStudioCommand(webRequest.studio()))
                .build();
    }

    private List<UpdateProjectCommand> toProjectCommand(List<ProjectWebRequest> projectWebRequests) {
        if (projectWebRequests == null)
            return List.of();

        return projectWebRequests.stream()
                .map(webRequest -> UpdateProjectCommand.builder()
                        .projectNo(webRequest.projectNo())
                        .projectName(webRequest.projectName())
                        .startDate(webRequest.startDate())
                        .endDate(webRequest.endDate())
                        .changeStatus(webRequest.changeStatus())
                        .build())
                .toList();
    }

    private List<UpdateSkillCommand> toSkillCommand(List<SkillWebRequest> skillWebRequests) {
        if (skillWebRequests == null)
            return List.of();

        return skillWebRequests.stream()
                .map(webRequest -> UpdateSkillCommand.builder()
                        .skillNo(webRequest.skillNo())
                        .skillType(webRequest.skillType())
                        .content(webRequest.content())
                        .changeStatus(webRequest.changeStatus())
                        .build())
                .toList();
    }

    private UpdateStudioCommand toStudioCommand(StudioWebRequest studioWebRequest) {
        return UpdateStudioCommand.builder()
                .studioNo(studioWebRequest.studioNo())
                .studioName(studioWebRequest.studioName())
                .employeesCount(studioWebRequest.employeesCount())
                .businessHours(studioWebRequest.businessHours())
                .address(studioWebRequest.address())
                .changeStatus(studioWebRequest.changeStatus())
                .build();
    }
}
