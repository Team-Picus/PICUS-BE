package com.picus.core.weekly_magazine.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.adapter.out.persistence.mapper.WeeklyMagazinePersistenceMapper;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazineJpaRepository;
import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class WeeklyMagazinePersistenceAdapter implements WeeklyMagazineReadPort {

    private final WeeklyMagazineJpaRepository weeklyMagazineJpaRepository;

    private final WeeklyMagazinePersistenceMapper mapper;

    @Override
    public Optional<WeeklyMagazine> findByWeekAt(int year, int month, int week) {
        WeekAt weekAt = WeekAt.builder()
                .year(year)
                .month(month)
                .week(week)
                .build();

        return weeklyMagazineJpaRepository.findByWeekAt(weekAt)
                .map(entity -> mapper.toDomain(entity, List.of()));
    }
}
