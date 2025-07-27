package com.picus.core.expert.adapter.out.persistence;


import com.picus.core.expert.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.expert.application.port.out.ExpertQueryPort;
import com.picus.core.expert.application.port.out.ExpertCommandPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
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
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@RequiredArgsConstructor
@PersistenceAdapter
public class ExpertQueryCommandPersistenceAdapter implements ExpertQueryPort, ExpertCommandPort {

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
    public Expert save(Expert expert, String userNo) {

        // ExpertEntity 저장
        ExpertEntity saved = saveExpertEntity(expert, userNo);

        // ProjectEntity 저장
        saveProjectEntities(saved, expert.getProjects());

        // SkillEntity 저장
        saveSkillEntities(saved, expert.getSkills());

        // StudioEntity 저장
        saveStudioEntities(saved, expert.getStudio());

        // Expert 도메인에 기본키 바인딩
        expert.bindExpertNo(saved.getExpertNo());

        return expert;
    }

    @Override
    public Optional<Expert> findById(String expertNo) {

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
    public void update(Expert expert) {
        ExpertEntity expertEntity = expertJpaRepository.findById(expert.getExpertNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // Expert 수정
        expertEntity.updateEntity(expert);
    }

    @Override
    public void update(Expert expert,
                       List<String> deletedProjectNos, List<String> deletedSkillNos, String deletedStudioNo) {

        ExpertEntity expertEntity = expertJpaRepository.findById(expert.getExpertNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // Expert 수정
        expertEntity.updateEntity(expert);

        // Project 수정
        updateProjectEntities(expert.getProjects(), expertEntity, deletedProjectNos);

        // Skill 수정
        updateSkillEntities(expert.getSkills(), expertEntity, deletedSkillNos);

        // Studio 수정
        updateStudio(expert.getStudio(), expertEntity, deletedStudioNo);
    }

    /**
     * private 메서드
     */

    private ExpertEntity saveExpertEntity(Expert expert, String userNo) {
        UserEntity userEntity = userJpaRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        ExpertEntity expertEntity = expertPersistenceMapper.mapToEntity(expert);
        expertEntity.bindUserEntity(userEntity);
        return expertJpaRepository.save(expertEntity);
    }

    private void saveProjectEntities(ExpertEntity expertEntity, List<Project> projects) {
        List<ProjectEntity> projectEntities = projects.stream()
                .map(project -> {
                    ProjectEntity projectEntity = projectPersistenceMapper.mapToEntity(project);
                    projectEntity.assignExpert(expertEntity);
                    return projectEntity;
                })
                .toList();
        projectJpaRepository.saveAll(projectEntities); // TODO:  jdbc batch insert
    }

    private void saveSkillEntities(ExpertEntity expertEntity, List<Skill> skills) {
        List<SkillEntity> skillEntities = skills.stream()
                .map(skill -> {
                    SkillEntity skillEntity = skillPersistenceMapper.mapToEntity(skill);
                    skillEntity.assignExpert(expertEntity);
                    return skillEntity;
                })
                .toList();
        skillJpaRepository.saveAll(skillEntities);
    }

    private void saveStudioEntities(ExpertEntity expertEntity, Studio studio) {
        if (studio != null) {
            StudioEntity studioEntity = studioPersistenceMapper.mapToEntity(studio);
            studioEntity.assignExpert(expertEntity);
            studioJpaRepository.save(studioEntity);
        }
    }

    private void updateProjectEntities(List<Project> projects, ExpertEntity expertEntity, List<String> deletedProjectNos) {

        // 삭제
        projectJpaRepository.deleteByProjectNoIn(deletedProjectNos);

        // 추가/수정
        for (Project project : projects) {
            String projectNo = project.getProjectNo();
            if (projectNo != null) {
                // PK가 있다 = 수정
                ProjectEntity projectEntity = projectJpaRepository.findById(projectNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));

                projectEntity.updateEntity(project);
            } else {
                // PK가 없다 = 저장
                ProjectEntity projectEntity = projectPersistenceMapper.mapToEntity(project);
                projectEntity.assignExpert(expertEntity);
                projectJpaRepository.save(projectEntity);
            }
        }
    }

    private void updateSkillEntities(List<Skill> skills, ExpertEntity expertEntity, List<String> deletedSkillNos) {

        // 삭제
        skillJpaRepository.deleteBySkillNoIn(deletedSkillNos);

        // 수정/추가
        for (Skill skill : skills) {
            String skillNo = skill.getSkillNo();
            if (skillNo != null) {
                // PK가 있다 = 수정
                SkillEntity skillEntity = skillJpaRepository.findById(skillNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                skillEntity.updateEntity(skill);
            } else {
                // PK가 없다 = 저장
                SkillEntity skillEntity = skillPersistenceMapper.mapToEntity(skill);
                skillEntity.assignExpert(expertEntity);
                skillJpaRepository.save(skillEntity);
            }
        }
    }

    private void updateStudio(Studio studio, ExpertEntity expertEntity, String deletedStudioNo) {

        // 삭제
        if(deletedStudioNo != null) {
            studioJpaRepository.deleteById(deletedStudioNo);
        } else {
            if (studio != null) {
                if (studio.getStudioNo() != null) {
                    // PK가 있다 = 수정
                    StudioEntity studioEntity = studioJpaRepository.findById(studio.getStudioNo())
                            .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                    studioEntity.updateStudio(studio);
                } else {
                    // PK가 없다 = 저장
                    StudioEntity studioEntity = studioPersistenceMapper.mapToEntity(studio);
                    studioEntity.assignExpert(expertEntity);
                    studioJpaRepository.save(studioEntity);
                }
            }
        }

    }

}
