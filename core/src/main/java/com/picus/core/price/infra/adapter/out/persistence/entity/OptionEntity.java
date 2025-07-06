package com.picus.core.price.infra.adapter.out.persistence.entity;

import com.picus.core.expert.infra.adapter.out.persistence.converter.StringConverter;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "options")
public class OptionEntity {

    @Id @Tsid
    private String optionsNo;

    private String name;
    private Integer count;
    private Integer price;
    @Convert(converter = StringConverter.class)
    private List<String> content = new ArrayList<>();
}
