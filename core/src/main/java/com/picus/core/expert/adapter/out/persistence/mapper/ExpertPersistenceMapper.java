package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ExpertPersistenceMapper {

    public Expert mapToDomain(ExpertEntity entity,
                              List<Project> projects,
                              List<Skill> skills,
                              Studio studio) {
        return Expert.builder()
                .expertNo(entity.getExpertNo())
                .backgroundImageKey(entity.getBackgroundImageKey())
                .intro(entity.getIntro())
                .activityCareer(entity.getActivityCareer())
                .activityAreas(entity.getActivityAreas())
                .activityCount(entity.getActivityCount())
                .lastActivityAt(entity.getLastActivityAt())
                .portfolioLinks(entity.getPortfolioLinks())
                .approvalStatus(entity.getApprovalStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .projects(projects)
                .skills(skills)
                .studio(studio)
                .build();
    }

    public ExpertEntity mapToEntity(Expert expert) {
        return ExpertEntity.builder()
                .backgroundImageKey(expert.getBackgroundImageKey())
                .intro(expert.getIntro())
                .activityCareer(expert.getActivityCareer())
                .activityAreas(expert.getActivityAreas())
                .activityCount(expert.getActivityCount())
                .lastActivityAt(expert.getLastActivityAt())
                .portfolioLinks(expert.getPortfolioLinks())
                .approvalStatus(expert.getApprovalStatus())
                .build();
    }
}
