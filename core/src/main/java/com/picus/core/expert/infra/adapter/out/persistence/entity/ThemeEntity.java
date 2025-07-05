package com.picus.core.expert.infra.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.ThemeType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

@Entity
@Table(name = "themes")
public class ThemeEntity {

    @Id @Tsid
    private String themeNo;

    @Enumerated(EnumType.STRING)
    private ThemeType themeType;
}
