package com.picus.core.weekly_magazine.application.port.in;

import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazineByWeekAtResult;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;

public interface LoadWeeklyMagazineByWeekAtUseCase {
    LoadWeeklyMagazineByWeekAtResult load(WeekAt weekAt);
}
