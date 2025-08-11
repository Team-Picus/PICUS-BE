package com.picus.core.weekly_magazine.adapter.in.web.data.response;

import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import lombok.Builder;

@Builder
public record LoadWeeklyMagazineByWeekAtResponse(
        String topic,
        String topicDescription,
        WeekAt weekAt,
        String thumbnailUrl
) {
}
