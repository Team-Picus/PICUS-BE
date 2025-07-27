package com.picus.core.expert.domain;

import com.picus.core.expert.domain.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SkillTest {

    @Test
    @DisplayName("모든 필드를 업데이트한다.")
    void updateSkill_allFields() {
        // given
        Skill skill = Skill.builder()
                .skillNo("SKILL-001")
                .skillType(SkillType.CAMERA)
                .content("카메라 능숙")
                .build();

        // when
        skill.updateSkill(SkillType.EDIT, "보정 가능");

        // then
        assertThat(skill.getSkillType()).isEqualTo(SkillType.EDIT);
        assertThat(skill.getContent()).isEqualTo("보정 가능");
    }

    @Test
    @DisplayName("null 값이 들어오면 기존 값을 유지한다.")
    void updateSkill_withNullValues() {
        // given
        Skill skill = Skill.builder()
                .skillNo("SKILL-002")
                .skillType(SkillType.LIGHT)
                .content("스타일링 잘함")
                .build();

        // when
        skill.updateSkill(null, null);

        // then
        assertThat(skill.getSkillType()).isEqualTo(SkillType.LIGHT);
        assertThat(skill.getContent()).isEqualTo("스타일링 잘함");
    }

    @Test
    @DisplayName("일부 필드만 업데이트한다.")
    void updateSkill_partialUpdate() {
        // given
        Skill skill = Skill.builder()
                .skillNo("SKILL-003")
                .skillType(SkillType.EDIT)
                .content("스타일링 고급")
                .build();

        // when
        skill.updateSkill(SkillType.CAMERA, null);

        // then
        assertThat(skill.getSkillType()).isEqualTo(SkillType.CAMERA);
        assertThat(skill.getContent()).isEqualTo("스타일링 고급");
    }
}