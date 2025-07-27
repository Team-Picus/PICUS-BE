package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.Portfolio;
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
                .portfolios(
                        new ArrayList<>(
                                Optional.ofNullable(entity.getPortfolioLinks())
                                        .orElse(new ArrayList<>())
                                        .stream()
                                        .map(link -> Portfolio.builder().link(link).build())
                                        .toList()
                        )
                )
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
                .portfolioLinks(
                        Optional.ofNullable(expert.getPortfolios())
                                .orElse(new ArrayList<>())
                                .stream()
                                .map(Portfolio::getLink) // Portfolio에서 URL 추출
                                .toList()
                )
                .approvalStatus(expert.getApprovalStatus())
                .build();
    }
}
