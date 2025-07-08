package com.picus.core.weekly_magazine.domain.model;


import java.util.List;

public class WeeklyMagazine {
    private String weeklyMagazineNo;

    private String topic;
    private String topicDescription;
    private String weekAt;
    private String thumbnailKey;
    private List<WeeklyMagazinePost> posts;
}
