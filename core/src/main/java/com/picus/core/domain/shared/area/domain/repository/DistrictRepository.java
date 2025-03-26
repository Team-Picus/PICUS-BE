package com.picus.core.domain.shared.area.domain.repository;

import com.picus.core.domain.shared.area.domain.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByName(String name);

    @Query("SELECT d FROM District d WHERE d.name IN :names")
    List<District> findPreferredAreas(List<String> names);

}
