package com.picus.core.weekly_magazine.domain.model;


import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeeklyMagazine {
    private String weeklyMagazineNo;

    private String topic;
    private String topicDescription;
    private WeekAt weekAt;
    private String thumbnailKey;
    private List<WeeklyMagazinePost> posts;
}
