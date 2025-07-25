package com.picus.core.expert.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.Project;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@Entity
@Table(name = "projects")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
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

    // 연관관계 편의 메서드
    public void assignExpert(ExpertEntity expertEntity) {
        this.expertEntity = expertEntity;
    }

    public void updateEntity(Project project) {
        this.projectName = project.getProjectName();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
    }
}
