package com.picus.core.expert.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "series")
public class SeriesEntity {

    @Id @Tsid
    private String seriesNo;

    private String name;
    private Boolean isFixed;
}
