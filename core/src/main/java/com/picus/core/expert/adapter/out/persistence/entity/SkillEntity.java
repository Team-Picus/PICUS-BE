package com.picus.core.expert.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.vo.SkillType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@Entity
@Table(name = "skills")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class SkillEntity {

    @Id @Tsid
    private String skillNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_no", nullable = false)
    private ExpertEntity expertEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillType skillType;

    @Column(nullable = false)
    private String content;

    public void assignExpert(ExpertEntity expertEntity) {
        this.expertEntity = expertEntity;
    }

    public void updateEntity(Skill skill) {
        this.skillType = skill.getSkillType();
        this.content = skill.getContent();
    }
}
