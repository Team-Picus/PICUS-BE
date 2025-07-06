package com.picus.core.expert.infra.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.SkillType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class SkillEntity {

    @Id @Tsid
    private String skillNo;

    @Enumerated(EnumType.STRING)
    private SkillType skillType;
    private String content;
}
