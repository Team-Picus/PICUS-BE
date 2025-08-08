package com.picus.core.weekly_magazine.application.port.in;

import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazinePostByWeekAtResult;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;

import java.util.List;

public interface LoadWeeklyMagazinePostByWeekAtUseCase {
    List<LoadWeeklyMagazinePostByWeekAtResult> load(WeekAt weekAt);
}
