package com.picus.core.weekly_magazine.application.port.out;

import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;

import java.util.Optional;

public interface WeeklyMagazineReadPort {

    Optional<WeeklyMagazine> findByWeekAt(int year, int month, int week);
}
