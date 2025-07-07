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
@Table(name = "packages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PackageEntity {

    @Id @Tsid
    private String packageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_no", nullable = false)
    private ThemeEntity themeEntity;
    private String name;
    private Integer price;
    @Convert(converter = StringConverter.class)
    private List<String> contents = new ArrayList<>();
    private String notice;
}
