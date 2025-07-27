package com.picus.core.expert.adapter.out.persistence.mapper;


import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.domain.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillPersistenceMapper {

    public Skill mapToDomain(SkillEntity entity) {
        return Skill.builder()
                .skillNo(entity.getSkillNo())
                .skillType(entity.getSkillType())
                .content(entity.getContent())
                .build();
    }

    public SkillEntity mapToEntity(Skill skill) {
        return SkillEntity.builder()
                .skillType(skill.getSkillType())
                .content(skill.getContent())
                .build();
    }
}
