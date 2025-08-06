package com.picus.core.weekly_magazine.adapter.out.persistence.repository;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyMagazineJpaRepository extends JpaRepository<WeeklyMagazine, String> {

}
