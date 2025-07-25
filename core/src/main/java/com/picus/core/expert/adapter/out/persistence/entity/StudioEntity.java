package com.picus.core.expert.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.Studio;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@Entity
@Table(name = "studios")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class StudioEntity {

    @Id @Tsid
    private String studioNo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_no")
    private ExpertEntity expertEntity;

    @Column(nullable = false)
    private String studioName;

    @Column(nullable = false)
    private Integer employeesCount;

    @Column(nullable = false)
    private String businessHours;

    @Column(nullable = false)
    private String address;

    public void assignExpert(ExpertEntity expertEntity) {
        this.expertEntity = expertEntity;
    }

    public void updateStudio(Studio studio) {
        this.studioName = studio.getStudioName();
        this.employeesCount = studio.getEmployeesCount();
        this.businessHours = studio.getBusinessHours();
        this.address = studio.getAddress();
    }
}
