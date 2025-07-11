package com.picus.core.expert.infra.adapter.out.persistence.repository;

import com.picus.core.expert.infra.adapter.out.persistence.entity.ExpertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertJpaRepository extends JpaRepository<ExpertEntity, String> {
}
