package com.picus.core.expert.infra.adapter.out.persistence.entity;

import com.picus.core.expert.infra.adapter.out.persistence.converter.StringConverter;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "packages")
public class PackageEntity {

    @Id @Tsid
    private String packageNo;

    private String name;
    private Integer price;
    @Convert(converter = StringConverter.class)
    private List<String> contents;
}
