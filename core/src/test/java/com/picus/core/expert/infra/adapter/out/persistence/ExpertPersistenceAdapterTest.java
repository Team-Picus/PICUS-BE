package com.picus.core.expert.infra.adapter.out.persistence;

import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.domain.model.vo.SkillType;
import com.picus.core.expert.infra.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.infra.adapter.out.persistence.mapper.ProjectPersistenceMapper;
import com.picus.core.expert.infra.adapter.out.persistence.mapper.SkillPersistenceMapper;
import com.picus.core.expert.infra.adapter.out.persistence.mapper.StudioPersistenceMapper;
import com.picus.core.expert.infra.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.infra.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.infra.adapter.out.persistence.repository.SkillJpaRepository;
import com.picus.core.expert.infra.adapter.out.persistence.repository.StudioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import({
        ExpertPersistenceAdapter.class,
        ExpertPersistenceMapper.class,
        ProjectPersistenceMapper.class,
        SkillPersistenceMapper.class,
        StudioPersistenceMapper.class
})
// TODO: 테스트 환경 데이터베이스 설정
@DataJpaTest
class ExpertPersistenceAdapterTest {

    @Autowired
    ExpertPersistenceAdapter expertPersistenceAdapter;

    @Autowired
    private ExpertJpaRepository expertJpaRepository;

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private SkillJpaRepository skillJpaRepository;

    @Autowired
    private StudioJpaRepository studioJpaRepository;

    @Test
    @DisplayName("Expert 도메인 객체를 저장하면 연관된 Project, Skill, Studio도 함께 저장된다")
    public void saveExpert() throws Exception {
        // given
        Expert expert = Expert.builder()
                .intro("소개입니다")
                .activityCareer("5년")
                .activityAreas(List.of(ActivityArea.SEOUL_GANGBUKGU))
                .activityDuration("2년")
                .activityCount(10)
                .recentlyActivityAt(LocalDateTime.of(2024, 5, 10, 10, 0))
                .portfolios(List.of(Portfolio.builder().link("http://portfolio.com").build()))
                .approvalStatus(ApprovalStatus.PENDING)
                .projects(List.of(
                        Project.builder()
                                .projectName("프로젝트 A")
                                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                                .endDate(LocalDateTime.of(2023, 12, 31, 18, 0))
                                .build()
                ))
                .skills(List.of(
                        Skill.builder()
                                .skillType(SkillType.CAMERA)
                                .content("카메라 전문가")
                                .build()
                ))
                .studio(
                        Studio.builder()
                                .studioName("포토 스튜디오")
                                .employeesCount(3)
                                .businessHours("09:00~18:00")
                                .address("서울 강남구")
                                .build()
                )
                .build();

        // when
        Expert saved = expertPersistenceAdapter.saveExpert(expert);

        // then
        assertThat(expertJpaRepository.findById(saved.getExpertNo())).isPresent();

        assertThat(projectJpaRepository.findAll())
                .anyMatch(p -> p.getProjectName().equals("프로젝트 A"));

        assertThat(skillJpaRepository.findAll())
                .anyMatch(s -> s.getContent().equals("카메라 전문가"));

        assertThat(studioJpaRepository.findAll())
                .anyMatch(s -> s.getStudioName().equals("포토 스튜디오"));
    }

}