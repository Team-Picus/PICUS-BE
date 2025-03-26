package com.picus.core.domain.expert.domain.repository;

import com.picus.core.domain.expert.domain.entity.area.ExpertDistrict;
import com.picus.core.domain.expert.domain.entity.area.ExpertDistrictId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertDistrictRepository extends JpaRepository<ExpertDistrict, ExpertDistrictId> {
}
