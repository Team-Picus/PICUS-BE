package com.picus.core.weekly_magazine.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeeklyMagazinePost {

    private String weeklyMagazinePostNo;
    private String postNo;
}
