package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.application.port.in.request.RequestApprovalCommand;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestApprovalWebMapper {

    public RequestApprovalCommand toCommand(RequestApprovalWebRequest webRequest, String userNo) {
        return RequestApprovalCommand.builder()
                .activityCareer(webRequest.activityCareer())
                .activityAreas(webRequest.activityAreas())
                .projects(toProjects(webRequest.projects()))
                .skills(toSkills(webRequest.skills()))
                .studio(toStudio(webRequest.studio()))
                .portfolioLinks(webRequest.portfolioLinks())
                .userNo(userNo)
                .build();
    }

    private List<Project> toProjects(List<RequestApprovalWebRequest.ProjectWebRequest> projectWebRequests) {
        return projectWebRequests.stream()
                .map(webRequest -> Project.builder()
                        .projectName(webRequest.projectName())
                        .startDate(webRequest.startDate())
                        .endDate(webRequest.endDate())
                        .build()
                ).toList();
    }

    private List<Skill> toSkills(List<RequestApprovalWebRequest.SkillWebRequest> skillWebRequests) {
        return skillWebRequests.stream()
                .map(webRequest -> Skill.builder()
                        .skillType(webRequest.skillType())
                        .content(webRequest.content())
                        .build()
                ).toList();
    }

    private Studio toStudio(RequestApprovalWebRequest.StudioWebRequest studioWebRequest) {
        return Studio.builder()
                .studioName(studioWebRequest.studioName())
                .employeesCount(studioWebRequest.employeesCount())
                .businessHours(studioWebRequest.businessHours())
                .address(studioWebRequest.address())
                .build();
    }

}
