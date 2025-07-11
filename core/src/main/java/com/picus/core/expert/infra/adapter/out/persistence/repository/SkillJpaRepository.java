package com.picus.core.expert.infra.adapter.out.persistence.repository;

import com.picus.core.expert.infra.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.infra.adapter.out.persistence.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillJpaRepository extends JpaRepository<SkillEntity, String> {
}
