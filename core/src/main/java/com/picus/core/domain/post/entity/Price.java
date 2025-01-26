package com.picus.core.domain.post.entity;

import lombok.Getter;

@Getter
public class Price {

    private Integer value;

    public Price(Integer value) {
        this.value = value;
    }
}