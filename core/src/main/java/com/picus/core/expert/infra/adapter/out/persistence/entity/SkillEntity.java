package com.picus.core.expert.infra.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.SkillType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "skills")
@NoArgsConstructor(access = PROTECTED)
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
}
