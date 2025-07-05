package com.picus.core.expert.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "theme_images")
public class ThemeImageEntity {

    @Id @Tsid
    private Long themeImageNo;

    private String key;
}
