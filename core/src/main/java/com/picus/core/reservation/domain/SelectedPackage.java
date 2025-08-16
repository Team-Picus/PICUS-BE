package com.picus.core.reservation.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SelectedPackage {

    private String name;
    private Integer price;
    private List<String> contents;
    private String notice;

}
