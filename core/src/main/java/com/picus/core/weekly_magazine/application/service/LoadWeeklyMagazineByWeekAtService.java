package com.picus.core.weekly_magazine.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.weekly_magazine.application.port.in.LoadWeeklyMagazineByWeekAtUseCase;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazineByWeekAtResult;
import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import lombok.RequiredArgsConstructor;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class LoadWeeklyMagazineByWeekAtService implements LoadWeeklyMagazineByWeekAtUseCase {

    private final WeeklyMagazineReadPort weeklyMagazineReadPort;
    @Override
    public LoadWeeklyMagazineByWeekAtResult load(WeekAt weekAt) {
        WeeklyMagazine weeklyMagazine = weeklyMagazineReadPort.findByWeekAt(weekAt.getYear(), weekAt.getMonth(), weekAt.getWeek())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        return LoadWeeklyMagazineByWeekAtResult.builder()
                .topic(weeklyMagazine.getTopic())
                .topicDescription(weeklyMagazine.getTopicDescription())
                .weekAt(weeklyMagazine.getWeekAt())
                .thumbnailUrl("") // TODO: file key -> url 변환 로직
                .build();
    }
}