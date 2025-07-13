package com.picus.core.expert.adapter.out.persistence;


import com.picus.core.expert.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.expert.application.port.out.SaveExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.adapter.out.persistence.mapper.ProjectPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.SkillPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.StudioPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.SkillJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ExpertPersistenceAdapter implements SaveExpertPort {

    // Jpa Repository
    private final ExpertJpaRepository expertJpaRepository;
    private final ProjectJpaRepository projectJpaRepository;
    private final SkillJpaRepository skillJpaRepository;
    private final StudioJpaRepository studioJpaRepository;

    // Mapper
    private final ExpertPersistenceMapper expertPersistenceMapper;
    private final ProjectPersistenceMapper projectPersistenceMapper;
    private final SkillPersistenceMapper skillPersistenceMapper;
    private final StudioPersistenceMapper studioPersistenceMapper;


    @Override
    public Expert saveExpert(Expert expert) {

        // ExpertEntity 저장
        ExpertEntity saved = saveExpertEntity(expert);

        // ProjectEntity 저장
        saveProjectEntity(expert, saved);

        // SkillEntity 저장
        saveSkillEntity(expert, saved);

        // StudioEntity 저장
        saveStudioEntity(expert, saved);

        // Expert 도메인에 기본키 바인딩
        expert.bindExpertNo(saved.getExpertNo());

        return expert;
    }


    private ExpertEntity saveExpertEntity(Expert expert) {
        ExpertEntity expertEntity = expertPersistenceMapper.mapToEntity(expert);
        return expertJpaRepository.save(expertEntity);
    }

    private void saveProjectEntity(Expert expert, ExpertEntity saved) {
        List<Project> projects = expert.getProjects();
        List<ProjectEntity> projectEntities = projects.stream()
                .map(project -> {
                    ProjectEntity projectEntity = projectPersistenceMapper.mapToEntity(project);
                    projectEntity.assignExpert(saved);
                    return projectEntity;
                })
                .toList();
        projectJpaRepository.saveAll(projectEntities); // TODO:  jdbc batch insert
    }

    private void saveSkillEntity(Expert expert, ExpertEntity saved) {
        List<Skill> skills = expert.getSkills();
        List<SkillEntity> skillEntities = skills.stream()
                .map(skill -> {
                    SkillEntity skillEntity = skillPersistenceMapper.mapToEntity(skill);
                    skillEntity.assignExpert(saved);
                    return skillEntity;
                })
                .toList();
        skillJpaRepository.saveAll(skillEntities);
    }

    private void saveStudioEntity(Expert expert, ExpertEntity saved) {
        Studio studio = expert.getStudio();
        StudioEntity studioEntity = studioPersistenceMapper.mapToEntity(studio);
        studioEntity.assignExpert(saved);
        studioJpaRepository.save(studioEntity);
    }
}
