package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.domain.model.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExpertTest {

    @Test
    @DisplayName("ActivityDuration은 생성된지 1개월 미만이면 일단위로 설정된다.")
    public void calculateActivityDuration_lessThanOneMonth() throws Exception {
        // given
        Expert expert = createExpert(LocalDateTime.of(2025, 6, 14, 15, 33));

        // when
        String activityDuration = expert.calculateActivityDuration(LocalDate.of(2025, 7, 13));

        // then
        assertThat(activityDuration).isEqualTo("29일");
    }

    @Test
    @DisplayName("ActivityDuration은 생성된지 1개월 이상이면 월단위로 설정된다.")
    public void calculateActivityDuration_moreThanOneMonth() throws Exception {
        // given
        Expert expert = createExpert(LocalDateTime.of(2025, 6, 13, 15, 33));

        // when
        String activityDuration = expert.calculateActivityDuration(LocalDate.of(2025, 7, 13));

        // then
        assertThat(activityDuration).isEqualTo("1개월");
    }

    @Test
    @DisplayName("ActivityDuration은 생성된지 1년 이상이면 년단위로 설정된다.")
    public void calculateActivityDuration_moreThanOneYear() throws Exception {
        // given
        Expert expert = createExpert(LocalDateTime.of(2024, 7, 13, 15, 33));

        // when
        String activityDuration = expert.calculateActivityDuration(LocalDate.of(2025, 7, 13));

        // then
        assertThat(activityDuration).isEqualTo("1년");
    }

    @Test
    @DisplayName("Expert의 ApprovalStatus를 APPROVAL로 바꾼다.")
    public void approveApprovalRequest() throws Exception {
        // given
        Expert expert = createExpert(ApprovalStatus.PENDING);

        // when
        expert.approveApprovalRequest();

        // then
        assertThat(expert.getApprovalStatus())
                .isEqualTo(ApprovalStatus.APPROVAL);
    }

    @Test
    @DisplayName("Expert의 ApprovalStatus를 REJECT로 바꾼다.")
    public void rejectApprovalRequest() throws Exception {
        // given
        Expert expert = createExpert(ApprovalStatus.PENDING);

        // when
        expert.rejectApprovalRequest();

        // then
        assertThat(expert.getApprovalStatus())
                .isEqualTo(ApprovalStatus.REJECT);
    }
    @Test
    @DisplayName("Expert의 기본정보를 바꾼다.")
    void updateBasicInfo_portfolios_not_null() {
        // given
        Expert expert = createExpert(
                new ArrayList<>(List.of(Portfolio.builder().link("https://old.link").build())),
                "original-key",
                "Old intro"
        );
        // when
        expert.updateBasicInfo("new-key", "https://new.link", "Updated intro");

        // then
        assertThat(expert.getPortfolios()).hasSize(2);
        assertThat(expert.getPortfolios().get(1).getLink()).isEqualTo("https://new.link");
        assertThat(expert.getBackgroundImageKey()).isEqualTo("new-key");
        assertThat(expert.getIntro()).isEqualTo("Updated intro");
    }

    @Test
    @DisplayName("전문가의 상세 정보 수정시,  activityCareer, activityAreas, projects, skills, studio를 수정한다.")
    void updateDetailInfo_success() {
        // given
        Expert expert = createExpert(
                "초기 경력",
                List.of("서울", "부산"),
                List.of(Project.builder()
                        .projectNo("P1")
                        .projectName("기존 프로젝트")
                        .startDate(LocalDateTime.of(2024, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2024, 6, 1, 0, 0))
                        .build()),
                List.of(Skill.builder()
                        .skillNo("S1")
                        .skillType(SkillType.CAMERA)
                        .content("Sony A7S3")
                        .build()),
                Studio.builder()
                        .studioNo("ST1")
                        .studioName("기존 스튜디오")
                        .employeesCount(5)
                        .businessHours("09:00~18:00")
                        .address("서울시 강남구")
                        .build()
        );

        // when
        expert.updateDetailInfo(
                "업데이트된 경력",
                List.of("대전", "광주"),
                List.of(
                        Project.builder()
                                .projectNo("P1")
                                .projectName("수정된 프로젝트")
                                .startDate(LocalDateTime.of(2024, 1, 10, 0, 0))
                                .endDate(LocalDateTime.of(2024, 6, 10, 0, 0))
                                .build(),
                        Project.builder()
                                .projectName("신규 프로젝트")
                                .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                                .endDate(LocalDateTime.of(2025, 6, 1, 0, 0))
                                .build()
                ),
                List.of(
                        Skill.builder()
                                .skillNo("S1")
                                .content("Canon R5")
                                .build(),
                        Skill.builder()
                                .skillType(SkillType.EDIT)
                                .content("Premiere Pro")
                                .build()
                ),
                Studio.builder()
                        .studioNo("ST1")
                        .studioName("업데이트된 스튜디오")
                        .employeesCount(8)
                        .businessHours("10:00~19:00")
                        .address("서울시 서초구")
                        .build()
        );

        // then
        assertThat(expert.getActivityCareer()).isEqualTo("업데이트된 경력");

        assertThat(expert.getActivityAreas()).containsExactly("대전", "광주");

        assertThat(expert.getProjects()).hasSize(2);
        assertThat(expert.getProjects())
                .anySatisfy(p -> {
                    if ("P1".equals(p.getProjectNo())) {
                        assertThat(p.getProjectName()).isEqualTo("수정된 프로젝트");
                        assertThat(p.getStartDate()).isEqualTo(LocalDateTime.of(2024, 1, 10, 0, 0));
                        assertThat(p.getEndDate()).isEqualTo(LocalDateTime.of(2024, 6, 10, 0, 0));
                    }
                });
        assertThat(expert.getProjects())
                .anySatisfy(p -> {
                    if (p.getProjectNo() == null) {
                        assertThat(p.getProjectName()).isEqualTo("신규 프로젝트");
                        assertThat(p.getStartDate()).isEqualTo(LocalDateTime.of(2025, 1, 1, 0, 0));
                        assertThat(p.getEndDate()).isEqualTo(LocalDateTime.of(2025, 6, 1, 0, 0));
                    }
                });

        assertThat(expert.getSkills()).hasSize(2);
        assertThat(expert.getSkills())
                .anySatisfy(s -> {
                    if ("S1".equals(s.getSkillNo())) {
                        assertThat(s.getContent()).isEqualTo("Canon R5");
                        assertThat(s.getSkillType()).isEqualTo(SkillType.CAMERA);
                    }
                });
        assertThat(expert.getSkills())
                .anySatisfy(s -> {
                    if (s.getSkillNo() == null) {
                        assertThat(s.getContent()).isEqualTo("Premiere Pro");
                        assertThat(s.getSkillType()).isEqualTo(SkillType.EDIT);
                    }
                });

        Studio updatedStudio = expert.getStudio();
        assertThat(updatedStudio.getStudioNo()).isEqualTo("ST1");
        assertThat(updatedStudio.getStudioName()).isEqualTo("업데이트된 스튜디오");
        assertThat(updatedStudio.getEmployeesCount()).isEqualTo(8);
        assertThat(updatedStudio.getBusinessHours()).isEqualTo("10:00~19:00");
        assertThat(updatedStudio.getAddress()).isEqualTo("서울시 서초구");
    }

    @Test
    @DisplayName("전문가의 상세 정보 수정시, null이 전달된 필드는 기존 값을 유지한다.")
    void updateDetailInfo_nullFields_shouldNotUpdate() {
        // given
        Expert expert = createExpert(
                "기존 경력",
                List.of("서울", "부산"),
                List.of(Project.builder()
                        .projectNo("P1")
                        .projectName("기존 프로젝트")
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 6, 1, 0, 0))
                        .build()),
                List.of(Skill.builder()
                        .skillNo("S1")
                        .skillType(SkillType.LIGHT)
                        .content("LED 라이트")
                        .build()),
                Studio.builder()
                        .studioNo("ST1")
                        .studioName("기존 스튜디오")
                        .employeesCount(3)
                        .businessHours("10:00~17:00")
                        .address("서울시 마포구")
                        .build()
        );

        // when: 모든 인자가 null
        expert.updateDetailInfo(null, null, null, null, null);

        // then: 아무것도 바뀌면 안 됨
        assertThat(expert.getActivityCareer()).isEqualTo("기존 경력");
        assertThat(expert.getActivityAreas()).containsExactly("서울", "부산");

        assertThat(expert.getProjects()).hasSize(1);
        Project p = expert.getProjects().get(0);
        assertThat(p.getProjectNo()).isEqualTo("P1");
        assertThat(p.getProjectName()).isEqualTo("기존 프로젝트");
        assertThat(p.getStartDate()).isEqualTo(LocalDateTime.of(2023, 1, 1, 0, 0));
        assertThat(p.getEndDate()).isEqualTo(LocalDateTime.of(2023, 6, 1, 0, 0));

        assertThat(expert.getSkills()).hasSize(1);
        Skill s = expert.getSkills().get(0);
        assertThat(s.getSkillNo()).isEqualTo("S1");
        assertThat(s.getSkillType()).isEqualTo(SkillType.LIGHT);
        assertThat(s.getContent()).isEqualTo("LED 라이트");

        Studio studio = expert.getStudio();
        assertThat(studio.getStudioNo()).isEqualTo("ST1");
        assertThat(studio.getStudioName()).isEqualTo("기존 스튜디오");
        assertThat(studio.getEmployeesCount()).isEqualTo(3);
        assertThat(studio.getBusinessHours()).isEqualTo("10:00~17:00");
        assertThat(studio.getAddress()).isEqualTo("서울시 마포구");
    }

    private Expert createExpert(LocalDateTime createdAt) {
        return Expert.builder()
                .createdAt(createdAt)
                .build();
    }

    private Expert createExpert(ApprovalStatus approvalStatus) {
        return Expert.builder()
                .approvalStatus(approvalStatus)
                .build();
    }

    private Expert createExpert(List<Portfolio> portfolios, String backgroundImageKey, String intro) {
        return Expert.builder()
                .backgroundImageKey(backgroundImageKey)
                .intro(intro)
                .portfolios(portfolios)
                .build();
    }

    private Expert createExpert(
            String activityCareer,
            List<String> activityAreas,
            List<Project> projects,
            List<Skill> skills,
            Studio studio
    ) {
        return Expert.builder()
                .activityCareer(activityCareer)
                .activityAreas(new ArrayList<>(activityAreas))
                .projects(new ArrayList<>(projects))
                .skills(new ArrayList<>(skills))
                .studio(studio)
                .build();
    }
}