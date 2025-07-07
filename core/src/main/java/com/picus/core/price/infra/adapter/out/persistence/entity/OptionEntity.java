package com.picus.core.price.infra.adapter.out.persistence.entity;

import com.picus.core.expert.infra.adapter.out.persistence.converter.StringConverter;
import com.picus.core.expert.infra.adapter.out.persistence.entity.ThemeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionEntity {

    @Id @Tsid
    private String optionsNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_no", nullable = false)
    private ThemeEntity themeEntity;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer count;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    @Convert(converter = StringConverter.class)
    private List<String> content = new ArrayList<>();
}
