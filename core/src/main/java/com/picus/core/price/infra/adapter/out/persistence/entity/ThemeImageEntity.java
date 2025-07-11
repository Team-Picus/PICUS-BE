package com.picus.core.price.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "theme_images")
@NoArgsConstructor(access = PROTECTED)
public class ThemeImageEntity {

    @Id @Tsid
    private Long themeImageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_no", nullable = false)
    private ThemeEntity themeEntity;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private Integer imageOrder;
}
