package com.picus.core.price.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class Option {
    private String optionNo;

    private String name;
    private Integer unitSize;
    private Integer pricePerUnit;
    private List<String> contents;

    public void updateOption(String name, Integer unitSize, Integer pricePerUnit, List<String> contents) {
        if(name != null)
            this.name = name;
        if(unitSize != null)
            this.unitSize = unitSize;
        if(pricePerUnit != null)
            this.pricePerUnit = pricePerUnit;
        if(contents != null)
            this.contents = contents;
    }
}
