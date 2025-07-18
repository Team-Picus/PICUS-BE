package com.picus.core.price.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class Option {
    private String name;
    private Integer count;
    private Integer price;
    private List<String> contents;
}
