package com.picus.core.expert.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "projects")
@NoArgsConstructor(access = PROTECTED)
public class ProjectEntity {

    @Id @Tsid
    private String projectNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_no", nullable = false)
    private ExpertEntity expertEntity;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

}
