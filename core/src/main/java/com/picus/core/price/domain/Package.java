package com.picus.core.price.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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

    public void updatePackage(String name, Integer price, List<String> contents, String notice) {
        if(name != null)
            this.name = name;
        if(price != null)
            this.price = price;
        if(contents != null)
            this.contents = contents;
        if(notice != null)
            this.notice = notice;
    }
}
