package com.picus.core.weekly_magazine.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.weekly_magazine.application.port.in.LoadWeeklyMagazineByWeekAtUseCase;
import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LoadWeeklyMagazineByWeekAtService implements LoadWeeklyMagazineByWeekAtUseCase {

    private final WeeklyMagazineReadPort weeklyMagazineReadPort;
    @Override
    public WeeklyMagazine load(WeekAt weekAt) {
        return weeklyMagazineReadPort.findByWeekAt(weekAt.getYear(), weekAt.getMonth(), weekAt.getWeek());
    }
}