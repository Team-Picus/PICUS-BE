package com.picus.core.weekly_magazine.application.port.in.result;

import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import lombok.Builder;

@Builder
public record LoadWeeklyMagazineByWeekAtResult(
        String topic,
        String topicDescription,
        WeekAt weekAt,
        String thumbnailUrl
) {
}
