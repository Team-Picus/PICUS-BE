package com.picus.core.domain.studio.domain.repository;

import com.picus.core.domain.studio.domain.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    boolean existsByExpertNo(Long expertNo);
}
