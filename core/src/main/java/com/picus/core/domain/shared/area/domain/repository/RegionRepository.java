package com.picus.core.domain.shared.area.domain.repository;

import com.picus.core.domain.shared.area.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
