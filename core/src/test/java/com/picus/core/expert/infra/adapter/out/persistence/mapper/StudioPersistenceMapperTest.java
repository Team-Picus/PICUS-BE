package com.picus.core.expert.infra.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.infra.adapter.out.persistence.entity.StudioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudioPersistenceMapperTest {

    private final StudioPersistenceMapper mapper = new StudioPersistenceMapper();

    @Test
    @DisplayName("StudioEntity 객체를 Studio 도메인으로 변환할 수 있다")
    void mapToDomain() {
        // Given
        StudioEntity entity = StudioEntity.builder()
                .studioName("스튜디오 포토")
                .employeesCount(4)
                .businessHours("10:00~19:00")
                .address("서울 강남구 테헤란로 123")
                .build();

        // When
        Studio result = mapper.mapToStudio(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStudioName()).isEqualTo("스튜디오 포토");
        assertThat(result.getEmployeesCount()).isEqualTo(4);
        assertThat(result.getBusinessHours()).isEqualTo("10:00~19:00");
        assertThat(result.getAddress()).isEqualTo("서울 강남구 테헤란로 123");
    }

    @Test
    @DisplayName("Studio 도메인 객체를 StudioEntity로 변환할 수 있다")
    void mapToEntity() {
        // Given
        Studio studio = Studio.builder()
                .studioName("뷰티 스튜디오")
                .employeesCount(7)
                .businessHours("09:00~21:00")
                .address("부산 해운대구 해운대로 456")
                .build();

        // When
        StudioEntity entity = mapper.mapToEntity(studio);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getStudioName()).isEqualTo("뷰티 스튜디오");
        assertThat(entity.getEmployeesCount()).isEqualTo(7);
        assertThat(entity.getBusinessHours()).isEqualTo("09:00~21:00");
        assertThat(entity.getAddress()).isEqualTo("부산 해운대구 해운대로 456");
    }
}