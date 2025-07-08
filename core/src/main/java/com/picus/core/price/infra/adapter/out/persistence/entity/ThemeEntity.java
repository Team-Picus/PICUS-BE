package com.picus.core.price.infra.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.ExpertThemeType;
import com.picus.core.expert.infra.adapter.out.persistence.entity.ExpertEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "themes")
@NoArgsConstructor(access = PROTECTED)
public class ThemeEntity {

    @Id @Tsid
    private String themeNo;

    @Column(nullable = false)
    private String expertNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpertThemeType expertThemeType;

}
