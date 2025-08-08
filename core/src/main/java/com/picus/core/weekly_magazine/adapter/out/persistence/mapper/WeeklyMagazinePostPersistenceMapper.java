package com.picus.core.weekly_magazine.adapter.out.persistence.mapper;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazinePostEntity;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import org.springframework.stereotype.Component;

@Component
public class WeeklyMagazinePostPersistenceMapper {

    public WeeklyMagazinePost toDomain(WeeklyMagazinePostEntity entity) {
        return WeeklyMagazinePost.builder()
                .weeklyMagazinePostNo(entity.getWeeklyMagazinePostNo())
                .postNo(entity.getPostNo())
                .build();
    }
}
