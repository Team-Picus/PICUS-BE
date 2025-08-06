package com.picus.core.weekly_magazine.adapter.out.persistence.repository;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazinePostEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyMagazinePostJpaRepository extends JpaRepository<WeeklyMagazinePostEntity, String> {

    List<WeeklyMagazinePostEntity> findByWeeklyMagazineEntity_WeekAt(WeekAt weekAt);
}
