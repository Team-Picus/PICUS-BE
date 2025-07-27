package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SkillPersistenceMapperTest {

    private final SkillPersistenceMapper mapper = new SkillPersistenceMapper();

    @Test
    @DisplayName("SkillEntity 객체를 Skill 도메인으로 변환할 수 있다")
    void mapToDomain() {
        // Given
        SkillEntity entity = SkillEntity.builder()
                .skillNo("skill_no")
                .skillType(SkillType.CAMERA)
                .content("캐논 5D 촬영 가능")
                .build();

        // When
        Skill result = mapper.mapToDomain(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSkillNo()).isEqualTo("skill_no");
        assertThat(result.getSkillType()).isEqualTo(SkillType.CAMERA);
        assertThat(result.getContent()).isEqualTo("캐논 5D 촬영 가능");
    }

    @Test
    @DisplayName("Skill 도메인 객체를 SkillEntity로 변환할 수 있다")
    void mapToEntity() {
        // Given
        Skill skill = Skill.builder()
                .skillType(SkillType.EDIT)
                .content("프리미어 프로, 애프터이펙트 가능")
                .build();

        // When
        SkillEntity entity = mapper.mapToEntity(skill);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getSkillType()).isEqualTo(SkillType.EDIT);
        assertThat(entity.getContent()).isEqualTo("프리미어 프로, 애프터이펙트 가능");
    }
}