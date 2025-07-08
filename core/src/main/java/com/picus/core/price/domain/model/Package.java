package com.picus.core.price.domain.model;

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
