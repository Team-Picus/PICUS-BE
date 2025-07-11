package com.picus.core.expert.infra.adapter.out.persistence.repository;

import com.picus.core.expert.infra.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.infra.adapter.out.persistence.entity.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioJpaRepository extends JpaRepository<StudioEntity, String> {
}
