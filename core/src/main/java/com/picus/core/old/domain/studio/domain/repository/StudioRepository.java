package com.picus.core.old.domain.studio.domain.repository;

import com.picus.core.old.domain.studio.domain.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    boolean existsByExpertNo(Long expertNo);

    Optional<Studio> findByExpertNo(Long expertNo);
}
