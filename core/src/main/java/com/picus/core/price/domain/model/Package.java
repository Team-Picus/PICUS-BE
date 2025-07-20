package com.picus.core.price.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class Package {

    private String packageNo;

    private String name;
    private Integer price;
    private List<String> contents;
    private String notice;
}
