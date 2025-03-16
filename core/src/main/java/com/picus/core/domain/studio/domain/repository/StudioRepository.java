package com.picus.core.domain.studio.domain.repository;

import com.picus.core.domain.studio.domain.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    Optional<Studio> findByExpertNo(Long expertNo);
}
