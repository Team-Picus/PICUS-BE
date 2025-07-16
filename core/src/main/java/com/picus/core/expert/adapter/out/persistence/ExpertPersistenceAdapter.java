package com.picus.core.expert.adapter.out.persistence;


import com.picus.core.expert.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.application.port.out.SaveExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
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
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@PersistenceAdapter
public class ExpertPersistenceAdapter implements SaveExpertPort, LoadExpertPort, UpdateExpertPort {

    // Jpa Repository
    private final ExpertJpaRepository expertJpaRepository;
    private final ProjectJpaRepository projectJpaRepository;
    private final SkillJpaRepository skillJpaRepository;
    private final StudioJpaRepository studioJpaRepository;

    private final UserJpaRepository userJpaRepository;

    // Mapper
    private final ExpertPersistenceMapper expertPersistenceMapper;
    private final ProjectPersistenceMapper projectPersistenceMapper;
    private final SkillPersistenceMapper skillPersistenceMapper;
    private final StudioPersistenceMapper studioPersistenceMapper;


    @Override
    public Expert saveExpert(Expert expert, String userNo) {

        // ExpertEntity 저장
        ExpertEntity saved = saveExpertEntity(expert, userNo);

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

    @Override
    public Optional<Expert> loadExpertByExpertNo(String expertNo) {

        // expert의 Project 가져오기
        List<Project> projects = projectJpaRepository.findByExpertEntity_ExpertNo(expertNo).stream()
                .map(projectPersistenceMapper::mapToDomain).toList();
        // expert의 Skill 가져오기
        List<Skill> skills = skillJpaRepository.findByExpertEntity_ExpertNo(expertNo).stream()
                .map(skillPersistenceMapper::mapToDomain).toList();
        // expert의 Studio 가져오기
        Studio studio = studioJpaRepository.findByExpertEntity_ExpertNo(expertNo)
                .map(studioPersistenceMapper::mapToDomain)
                .orElse(null);

        return expertJpaRepository.findById(expertNo)
                .map(expertEntity -> expertPersistenceMapper.mapToDomain(expertEntity, projects, skills, studio));
    }

    @Override
    public void updateExpert(Expert expert) {
        Optional<ExpertEntity> expertEntity = expertJpaRepository.findById(expert.getExpertNo());
        expertEntity.ifPresent(entity -> entity.updateEntity(expert));
    }

    /**
     * private 메서드
     */

    private ExpertEntity saveExpertEntity(Expert expert, String userNo) {
        UserEntity userEntity = userJpaRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
        ExpertEntity expertEntity = expertPersistenceMapper.mapToEntity(expert);
        expertEntity.bindUserEntity(userEntity);
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
