package com.picus.core.weekly_magazine.application.port.out;

import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;

import java.util.List;
import java.util.Optional;

public interface WeeklyMagazineReadPort {

    Optional<WeeklyMagazine> findByWeekAt(int year, int month, int week);

    List<WeeklyMagazinePost> findWeeklyMagazinePostByWeekAt(int year, int month, int week);
}
