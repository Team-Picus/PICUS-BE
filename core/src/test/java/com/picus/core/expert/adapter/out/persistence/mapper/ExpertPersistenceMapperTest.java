package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExpertPersistenceMapperTest {

    private final ExpertPersistenceMapper mapper = new ExpertPersistenceMapper();

    @Test
    @DisplayName("ExpertEntity 객체를 Expert 도메인으로 변환할 수 있다")
    void mapToDomain() {
        // Given
        ExpertEntity entity = ExpertEntity.builder()
                .expertNo("EXP456")
                .backgroundImageKey("img-key")
                .intro("전문가 소개")
                .activityCareer("경력 5년")
                .activityAreas(List.of(ActivityArea.SEOUL_GANGBUKGU))
                .activityCount(8)
                .lastActivityAt(LocalDateTime.of(2024, 5, 20, 10, 30))
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .createdAt(LocalDateTime.of(2024, 5, 20, 10, 30))
                .updatedAt(LocalDateTime.of(2024, 5, 21, 10, 30))
                .deletedAt(LocalDateTime.of(2024, 5, 22, 10, 30))
                .build();


        // When
        Expert domain = mapper.mapToDomain(entity);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getExpertNo()).isEqualTo("EXP456");
        assertThat(domain.getBackgroundImageKey()).isEqualTo("img-key");
        assertThat(domain.getIntro()).isEqualTo("전문가 소개");
        assertThat(domain.getActivityCareer()).isEqualTo("경력 5년");
        assertThat(domain.getActivityAreas()).containsExactly(ActivityArea.SEOUL_GANGBUKGU);
        assertThat(domain.getActivityCount()).isEqualTo(8);
        assertThat(domain.getLastActivityAt()).isEqualTo(LocalDateTime.of(2024, 5, 20, 10, 30));
        assertThat(domain.getPortfolios()).extracting(Portfolio::getLink).containsExactly("http://myportfolio.com");
        assertThat(domain.getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
        assertThat(domain.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 5, 20, 10, 30));
        assertThat(domain.getUpdatedAt()).isEqualTo(LocalDateTime.of(2024, 5, 21, 10, 30));
        assertThat(domain.getDeletedAt()).isEqualTo(LocalDateTime.of(2024, 5, 22, 10, 30));
    }

    @Test
    @DisplayName("Expert 도메인 객체를 ExpertEntity로 변환할 수 있다")
    void mapToEntity() {
        // Given
        Expert domain = Expert.builder()
                .expertNo("EXP123")
                .backgroundImageKey("bg-key")
                .intro("소개입니다")
                .activityCareer("10년 경력")
                .activityAreas(List.of(ActivityArea.SEOUL_GANGBUKGU))
                .activityDuration("3년")
                .activityCount(15)
                .lastActivityAt(LocalDateTime.of(2023, 1, 1, 12, 0))
                .portfolios(List.of(Portfolio.builder().link("http://portfolio.com").build()))
                .approvalStatus(ApprovalStatus.APPROVAL)
                .build();

        // When
        ExpertEntity entity = mapper.mapToEntity(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getExpertNo()).isEqualTo("EXP123");
        assertThat(entity.getBackgroundImageKey()).isEqualTo("bg-key");
        assertThat(entity.getIntro()).isEqualTo("소개입니다");
        assertThat(entity.getActivityCareer()).isEqualTo("10년 경력");
        assertThat(entity.getActivityAreas()).containsExactly(ActivityArea.SEOUL_GANGBUKGU);
        assertThat(entity.getActivityCount()).isEqualTo(15);
        assertThat(entity.getLastActivityAt()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(entity.getPortfolioLinks()).containsExactly("http://portfolio.com");
        assertThat(entity.getApprovalStatus()).isEqualTo(ApprovalStatus.APPROVAL);
    }
}
