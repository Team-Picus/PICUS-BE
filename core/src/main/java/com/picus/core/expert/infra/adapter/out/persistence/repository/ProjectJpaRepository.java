package com.picus.core.expert.infra.adapter.out.persistence.repository;

import com.picus.core.expert.infra.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.infra.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, String> {
}
