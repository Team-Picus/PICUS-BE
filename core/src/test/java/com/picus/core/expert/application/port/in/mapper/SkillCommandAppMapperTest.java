package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.ChangeStatus;
import com.picus.core.expert.application.port.in.command.SkillCommand;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SkillCommandAppMapperTest {

    private final SkillCommandAppMapper mapper = new SkillCommandAppMapper();

    @Test
    @DisplayName("SkillCommand를 Skill 도메인 객체로 변환한다.")
    void toDomain_success() {
        // given
        SkillCommand command = SkillCommand.builder()
                .skillNo("SKILL-001")
                .skillType(SkillType.CAMERA)
                .content("카메라 잘 다룸")
                .changeStatus(ChangeStatus.NEW)
                .build();

        // when
        Skill result = mapper.toDomain(command);

        // then
        assertThat(result.getSkillNo()).isEqualTo("SKILL-001");
        assertThat(result.getSkillType()).isEqualTo(SkillType.CAMERA);
        assertThat(result.getContent()).isEqualTo("카메라 잘 다룸");
    }
}