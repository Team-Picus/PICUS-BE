package com.picus.core.weekly_magazine.application.port.out;

import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;

public interface WeeklyMagazineReadPort {

    WeeklyMagazine findByWeekAt(int year, int month, int week);
}
