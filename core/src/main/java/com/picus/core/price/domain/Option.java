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
    private Integer count;
    private Integer price;
    private List<String> contents;

    public void updateOption(String name, Integer count, Integer price, List<String> contents) {
        if(name != null)
            this.name = name;
        if(count != null)
            this.count = count;
        if(price != null)
            this.price = price;
        if(contents != null)
            this.contents = contents;
    }
}
