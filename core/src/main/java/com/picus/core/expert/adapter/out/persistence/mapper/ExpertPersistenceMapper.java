package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class ExpertPersistenceMapper {

    public Expert mapToDomain(ExpertEntity entity) {
        Expert expert = Expert.builder()
                .expertNo(entity.getExpertNo())
                .backgroundImageKey(entity.getBackgroundImageKey())
                .intro(entity.getIntro())
                .activityCareer(entity.getActivityCareer())
                .activityAreas(entity.getActivityAreas())
                .activityCount(entity.getActivityCount())
                .lastActivityAt(entity.getLastActivityAt())
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
        expert.calculateActivityDuration(LocalDate.now());
        return expert;
    }

    public ExpertEntity mapToEntity(Expert expert) {
        return ExpertEntity.builder()
                .expertNo(expert.getExpertNo())
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
