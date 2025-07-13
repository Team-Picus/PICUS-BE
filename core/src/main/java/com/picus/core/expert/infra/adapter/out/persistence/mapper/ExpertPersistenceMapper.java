package com.picus.core.expert.infra.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.infra.adapter.out.persistence.entity.ExpertEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class ExpertPersistenceMapper {

    public Expert mapToDomain(ExpertEntity entity) {
        return Expert.builder()
                .expertNo(entity.getExpertNo())
                .backgroundImageKey(entity.getBackgroundImageKey())
                .intro(entity.getIntro())
                .activityCareer(entity.getActivityCareer())
                .activityAreas(entity.getActivityAreas())
                .activityDuration(entity.getActivityDuration())
                .activityCount(entity.getActivityCount())
                .recentlyActivityAt(entity.getRecentlyActivityAt())
                .portfolios(
                        Optional.ofNullable(entity.getPortfolioLinks())
                                .orElse(new ArrayList<>())
                                .stream()
                                .map(link -> Portfolio.builder().link(link).build()) // URL → Portfolio 객체 생성
                                .toList()
                )
                .approvalStatus(entity.getApprovalStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    public ExpertEntity mapToEntity(Expert expert) {
        return ExpertEntity.builder()
                .expertNo(expert.getExpertNo())
                .backgroundImageKey(expert.getBackgroundImageKey())
                .intro(expert.getIntro())
                .activityCareer(expert.getActivityCareer())
                .activityAreas(expert.getActivityAreas())
                .activityDuration(expert.getActivityDuration())
                .activityCount(expert.getActivityCount())
                .recentlyActivityAt(expert.getRecentlyActivityAt())
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
