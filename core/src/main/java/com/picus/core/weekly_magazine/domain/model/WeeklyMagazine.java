package com.picus.core.weekly_magazine.domain.model;


import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;

import java.util.List;

public class WeeklyMagazine {
    private String weeklyMagazineNo;

    private String topic;
    private String topicDescription;
    private WeekAt weekAt;
    private String thumbnailKey;
    private List<WeeklyMagazinePost> posts;
}
