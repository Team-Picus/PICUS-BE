package com.picus.core.expert.adapter.out.persistence.repository;

import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillJpaRepository extends JpaRepository<SkillEntity, String> {

    List<SkillEntity> findByExpertEntity_ExpertNo(String expertNo);

    void deleteBySkillNoIn(List<String> deletedSkillNos);
}
