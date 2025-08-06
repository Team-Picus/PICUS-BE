package com.picus.core.weekly_magazine.application.port.in;

import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;

public interface LoadWeeklyMagazineByWeekAtUseCase {
    WeeklyMagazine load(WeekAt weekAt);
}
