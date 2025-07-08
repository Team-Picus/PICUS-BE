package com.picus.core.price.domain.model;

import com.picus.core.expert.domain.model.vo.ExpertThemeType;

import java.util.ArrayList;
import java.util.List;

public class Theme {

    private String themeNo;

    private ExpertThemeType expertThemeType;
    private List<ThemeImage> themeImages = new ArrayList<>();
    private List<Package> packages;
    private List<Option> options;
}
