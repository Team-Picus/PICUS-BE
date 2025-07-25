package com.picus.core.expert.adapter.out.persistence.repository;

import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudioJpaRepository extends JpaRepository<StudioEntity, String> {
    Optional<StudioEntity> findByExpertEntity_ExpertNo(String expertNo);
}
