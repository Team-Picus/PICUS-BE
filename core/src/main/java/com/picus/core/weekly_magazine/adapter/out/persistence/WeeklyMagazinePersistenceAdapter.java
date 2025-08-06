package com.picus.core.weekly_magazine.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazinePostEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.adapter.out.persistence.mapper.WeeklyMagazinePersistenceMapper;
import com.picus.core.weekly_magazine.adapter.out.persistence.mapper.WeeklyMagazinePostPersistenceMapper;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazineJpaRepository;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazinePostJpaRepository;
import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class WeeklyMagazinePersistenceAdapter implements WeeklyMagazineReadPort {

    private final WeeklyMagazineJpaRepository magazineRepo;
    private final WeeklyMagazinePostJpaRepository magazinePostRepo;

    private final WeeklyMagazinePersistenceMapper magazineMapper;
    private final WeeklyMagazinePostPersistenceMapper magazinePostMapper;

    @Override
    public Optional<WeeklyMagazine> findByWeekAt(int year, int month, int week) {
        // WeekAt 생성
        WeekAt weekAt = WeekAt.builder()
                .year(year)
                .month(month)
                .week(week)
                .build();

        // 조회 및 반환
        return magazineRepo.findByWeekAt(weekAt)
                .map(entity -> magazineMapper.toDomain(entity, List.of()));
    }

    @Override
    public List<WeeklyMagazinePost> findWeeklyMagazinePostByWeekAt(int year, int month, int week) {

        // WeekAt 생성
        WeekAt weekAt = WeekAt.builder()
                .year(year)
                .month(month)
                .week(week)
                .build();

        // 조회
        List<WeeklyMagazinePostEntity> magazinePostEntities = magazinePostRepo.findByWeeklyMagazineEntity_WeekAt(weekAt);

        // 반환
        return magazinePostEntities.stream()
                .map(magazinePostMapper::toDomain)
                .toList();
    }
}
