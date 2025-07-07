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
    private String name;
    private Integer count;
    private Integer price;
    @Convert(converter = StringConverter.class)
    private List<String> content = new ArrayList<>();
}
