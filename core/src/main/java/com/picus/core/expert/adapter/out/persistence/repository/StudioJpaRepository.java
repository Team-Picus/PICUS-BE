package com.picus.core.expert.adapter.out.persistence.repository;

import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioJpaRepository extends JpaRepository<StudioEntity, String> {
}
