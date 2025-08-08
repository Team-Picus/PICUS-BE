package com.picus.core.weekly_magazine.adapter.out.persistence.mapper;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazineEntity;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeeklyMagazinePersistenceMapper {

    public WeeklyMagazine toDomain(WeeklyMagazineEntity weeklyMagazineEntity, List<WeeklyMagazinePost> weeklyMagazinePosts) {
        return WeeklyMagazine.builder()
                .weeklyMagazineNo(weeklyMagazineEntity.getWeeklyMagazineNo())
                .topic(weeklyMagazineEntity.getTopic())
                .topicDescription(weeklyMagazineEntity.getTopicDescription())
                .weekAt(toDomainWeekAt(weeklyMagazineEntity))
                .thumbnailKey(weeklyMagazineEntity.getThumbnailKey())
                .weeklyMagazinePosts(weeklyMagazinePosts)
                .build();
    }

    private WeekAt toDomainWeekAt(WeeklyMagazineEntity weeklyMagazineEntity) {
        return WeekAt.builder()
                .year(weeklyMagazineEntity.getWeekAt().getYear())
                .month(weeklyMagazineEntity.getWeekAt().getMonth())
                .week(weeklyMagazineEntity.getWeekAt().getWeek())
                .build();
    }
}
