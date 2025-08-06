package com.picus.core.weekly_magazine.adapter.out.persistence.repository;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazineEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeeklyMagazineJpaRepository extends JpaRepository<WeeklyMagazineEntity, String> {

    Optional<WeeklyMagazineEntity> findByWeekAt(WeekAt weekAt);
}
