package com.picus.core.weekly_magazine.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class WeeklyMagazinePersistenceAdapter implements WeeklyMagazineReadPort {
    @Override
    public WeeklyMagazine findByWeekAt(int year, int month, int week) {
        return null;
    }
}
