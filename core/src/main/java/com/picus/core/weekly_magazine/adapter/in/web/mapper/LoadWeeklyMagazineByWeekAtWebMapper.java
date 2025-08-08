package com.picus.core.weekly_magazine.adapter.in.web.mapper;

import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazineByWeekAtResponse;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazineByWeekAtResult;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import org.springframework.stereotype.Component;

@Component
public class LoadWeeklyMagazineByWeekAtWebMapper {
    public LoadWeeklyMagazineByWeekAtResponse toResponse(LoadWeeklyMagazineByWeekAtResult result) {
        return LoadWeeklyMagazineByWeekAtResponse.builder()
                .topic(result.topic())
                .topicDescription(result.topicDescription())
                .weekAt(result.weekAt())
                .thumbnailUrl(result.thumbnailUrl())
                .build();
    }
}
