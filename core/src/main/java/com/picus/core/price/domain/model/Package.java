package com.picus.core.price.domain.model;

import com.picus.core.expert.infra.adapter.out.persistence.converter.StringConverter;
import com.picus.core.expert.infra.adapter.out.persistence.entity.ThemeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Package {

    private String packageNo;

    private String themeNo;
    private String name;
    private Integer price;
    private List<String> contents = new ArrayList<>();
    private String notice;
}
