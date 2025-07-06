package com.picus.core.expert.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id @Tsid
    private String projectNo;

    private String projectName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
