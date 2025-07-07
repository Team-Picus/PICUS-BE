package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.ExpertThemeType;

import java.util.ArrayList;
import java.util.List;

public class Theme {
    ExpertThemeType expertThemeType;
    List<ThemeImage> themeImages = new ArrayList<>();
}
