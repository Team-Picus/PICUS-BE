package com.picus.core.expert.domain;

import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.expert.domain.vo.Portfolio;
import com.picus.core.expert.domain.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
        expert.updateBasicInfo("new-key", List.of("https://changed.link", "https://new.link"), "Updated intro");

        // then
        assertThat(expert.getPortfolios()).hasSize(2)
                .extracting(Portfolio::getLink)
                .containsExactlyInAnyOrder("https://changed.link", "https://new.link");
        assertThat(expert.getBackgroundImageKey()).isEqualTo("new-key");
        assertThat(expert.getIntro()).isEqualTo("Updated intro");
    }

    @Test
    @DisplayName("전문가의 상세 정보 수정시,  activityCareer, activityAreas, projects, skills, studio를 수정한다.")
    void updateDetailInfo_success() {
        // given
        Expert expert = createExpert(
                "초기 경력",
                List.of("서울", "부산")
        );

        // when
        expert.updateDetailInfo(
                "업데이트된 경력",
                List.of("대전", "광주")
        );

        // then
        assertThat(expert.getActivityCareer()).isEqualTo("업데이트된 경력");

        assertThat(expert.getActivityAreas()).containsExactly("대전", "광주");
    }

    @Test
    @DisplayName("전문가의 상세 정보 수정시, null이 전달된 필드는 기존 값을 유지한다.")
    void updateDetailInfo_nullFields_shouldNotUpdate() {
        // given
        Expert expert = createExpert(
                "기존 경력",
                List.of("서울", "부산")
        );

        // when: 모든 인자가 null
        expert.updateDetailInfo(null, null);

        // then: 아무것도 바뀌면 안 됨
        assertThat(expert.getActivityCareer()).isEqualTo("기존 경력");
        assertThat(expert.getActivityAreas()).containsExactly("서울", "부산");
    }

    @Test
    @DisplayName("전문가의 Project를 추가한다.")
    public void addProject() throws Exception {
        // given
        Expert expert = Expert.builder().projects(List.of(
                        createProject("pj_no1", "pj_name1",
                                LocalDateTime.of(2025, 7, 20, 10, 10),
                                LocalDateTime.of(2025, 7, 20, 10, 20))
                ))
                .build();

        Project newProject = createProject("pj_no2", "pj_name2",
                LocalDateTime.of(2023, 10, 10, 11, 10),
                LocalDateTime.of(2023, 10, 15, 12, 20));

        // when
        expert.addProject(newProject);

        // then
        List<Project> projects = expert.getProjects();

        assertThat(projects).hasSize(2)
                .extracting(
                        Project::getProjectNo,
                        Project::getProjectName,
                        Project::getStartDate,
                        Project::getEndDate
                ).containsExactlyInAnyOrder(
                        tuple("pj_no1", "pj_name1",
                                LocalDateTime.of(2025, 7, 20, 10, 10),
                                LocalDateTime.of(2025, 7, 20, 10, 20)),
                        tuple("pj_no2", "pj_name2",
                                LocalDateTime.of(2023, 10, 10, 11, 10),
                                LocalDateTime.of(2023, 10, 15, 12, 20))
                );
    }

    @Test
    @DisplayName("기존 프로젝트 정보를 업데이트한다.")
    void updateProject() {
        // given
        Expert expert = Expert.builder()
                .projects(List.of(
                                createProject("PRJ-001", "이전 프로젝트",
                                        LocalDateTime.of(2024, 1, 1, 0, 0),
                                        LocalDateTime.of(2024, 12, 31, 0, 0)
                                )
                        )
                )
                .build();

        Project updatedProject = createProject("PRJ-001", "변경된 프로젝트명",
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 12, 31, 0, 0)
        );

        // when
        expert.updateProject(updatedProject);

        // then
        List<Project> projects = expert.getProjects();
        assertThat(projects).hasSize(1)
                .extracting(
                        Project::getProjectNo,
                        Project::getProjectName,
                        Project::getStartDate,
                        Project::getEndDate
                ).containsExactlyInAnyOrder(
                        tuple("PRJ-001", "변경된 프로젝트명",
                                LocalDateTime.of(2025, 1, 1, 0, 0),
                                LocalDateTime.of(2025, 12, 31, 0, 0))
                );
    }

    @Test
    @DisplayName("기존 프로젝트를 삭제한다.")
    void deleteProject() {
        // given

        Expert expert = Expert.builder()
                .projects(List.of(
                                createProject("PRJ-001", "이전 프로젝트",
                                        LocalDateTime.of(2024, 1, 1, 0, 0),
                                        LocalDateTime.of(2024, 12, 31, 0, 0)
                                )
                        )
                )
                .build();

        // when
        expert.deleteProject("PRJ-001");

        // then
        List<Project> projects = expert.getProjects();
        assertThat(projects).isEmpty();
    }

    @Test
    @DisplayName("새로운 스킬을 추가한다.")
    public void addSkill() throws Exception {
        // given
        Expert expert = Expert.builder().skills(
                List.of(createSkill("skill1", SkillType.EDIT, "content"))
        ).build();

        Skill newSkill = createSkill(null, SkillType.CAMERA, "new_content");

        // when
        expert.addSkill(newSkill);

        // then
        List<Skill> skills = expert.getSkills();

        assertThat(skills).hasSize(2)
                .extracting(
                        Skill::getSkillType,
                        Skill::getContent
                ).containsExactlyInAnyOrder(
                        tuple(SkillType.EDIT, "content"),
                        tuple(SkillType.CAMERA, "new_content")
                );
    }

    @Test
    @DisplayName("기존 스킬 정보를 업데이트한다.")
    void updateSkill() {
        // given
        Expert expert = Expert.builder()
                .skills(List.of(
                        createSkill("SKILL-001", SkillType.CAMERA, "기존 설명")
                )).build();

        Skill updatedSkill = createSkill("SKILL-001", SkillType.LIGHT, "업데이트된 설명");

        // when
        expert.updateSkill(updatedSkill);

        // then
        assertThat(expert.getSkills()).hasSize(1)
                .extracting(
                        Skill::getSkillType,
                        Skill::getContent
                ).containsExactlyInAnyOrder(
                        tuple(SkillType.LIGHT, "업데이트된 설명")
                );
    }

    @Test
    @DisplayName("기존 스킬을 삭제한다.")
    public void deleteSkill() throws Exception {
        // given
        Expert expert = Expert.builder()
                .skills(List.of(
                        createSkill("SKILL-001", SkillType.CAMERA, "기존 설명")
                )).build();


        // when
        expert.deleteSkill("SKILL-001");

        // then
        assertThat(expert.getSkills()).isEmpty();
    }

    @Test
    @DisplayName("스튜디오를 추가한다.")
    public void addStudio() throws Exception {
        // given
        Expert expert = Expert.builder().studio(null).build();

        Studio newStudio = createStudio(null, "픽어스 스튜디오", 5,
                "10:00~19:00", "서울 강남구");

        // when
        expert.addStudio(newStudio);

        // then
        Studio studio = expert.getStudio();
        assertThat(studio).isNotNull()
                .extracting(
                        Studio::getStudioName,
                        Studio::getEmployeesCount,
                        Studio::getBusinessHours,
                        Studio::getAddress
                ).containsExactlyInAnyOrder(
                        "픽어스 스튜디오", 5, "10:00~19:00", "서울 강남구"
                );
    }

    @Test
    @DisplayName("스튜디오를 수정한다.")
    public void updateStudio() throws Exception {
        // given
        Expert expert = Expert.builder().studio(
                createStudio("ST-001", "기존 스튜디오", 3,
                        "09:00~18:00", "서울시 강남구")
        ).build();

        Studio updatedStudio = createStudio("ST-001", "업데이트된 스튜디오", 5,
                "10:00~19:00", "서울시 서초구");

        // when
        expert.updateStudio(updatedStudio);

        // then
        Studio studio = expert.getStudio();
        assertThat(studio).isNotNull()
                .extracting(
                        Studio::getStudioNo,
                        Studio::getStudioName,
                        Studio::getEmployeesCount,
                        Studio::getBusinessHours,
                        Studio::getAddress
                ).containsExactlyInAnyOrder(
                        "ST-001", "업데이트된 스튜디오", 5, "10:00~19:00", "서울시 서초구"
                );
    }

    @Test
    @DisplayName("마지막 활동시간(lastActivityAt)을 업데이트 한다.")
    public void updateLastActivityAt() throws Exception {
        // given
        Expert expert = Expert.builder()
                .lastActivityAt(LocalDateTime.of(2020, 10, 10, 10, 10))
                .build();

        // when
        expert.updateLastActivityAt(LocalDateTime.of(2022, 5, 5, 5, 5));

        // then
        assertThat(expert.getLastActivityAt())
                .isEqualTo(LocalDateTime.of(2022, 5, 5, 5, 5));
    }

    @Test
    @DisplayName("전문가의 activityCount를 1 증가시킨다.")
    public void increaseActivityCount_success() throws Exception {
        // given
        Expert expert = createExpert(10);

        // when
        expert.increaseActivityCount();

        // then
        assertThat(expert.getActivityCount()).isEqualTo(11);
    }

    @Test
    @DisplayName("전문가의 activityCount를 1 감소시킨다.")
    public void decreaseActivityCount_success() throws Exception {
        // given
        Expert expert = createExpert(10);

        // when
        expert.decreaseActivityCount();

        // then
        assertThat(expert.getActivityCount()).isEqualTo(9);
    }

    @Test
    @DisplayName("전문가의 activityCount가 0이면 줄어들지 않는다..")
    public void decreaseActivityCount_ifZero() throws Exception {
        // given
        Expert expert = createExpert(0);

        // when
        expert.decreaseActivityCount();

        // then
        assertThat(expert.getActivityCount()).isEqualTo(0);
    }

    private Expert createExpert(LocalDateTime createdAt) {
        return Expert.builder()
                .createdAt(createdAt)
                .build();
    }

    private Expert createExpert(Integer activityCount) {
        return Expert.builder()
                .activityCount(activityCount)
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
            List<String> activityAreas
    ) {
        return Expert.builder()
                .activityCareer(activityCareer)
                .activityAreas(new ArrayList<>(activityAreas))
                .build();
    }

    private Project createProject(String projectNo, String projectName, LocalDateTime startDate, LocalDateTime endDate) {
        return Project.builder()
                .projectNo(projectNo)
                .projectName(projectName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    private Skill createSkill(String skillNo, SkillType skillType, String content) {
        return Skill.builder()
                .skillNo(skillNo)
                .skillType(skillType)
                .content(content)
                .build();
    }

    private Studio createStudio(String studioNo, String studioName, int employeesCount, String businessHours, String address) {
        return Studio.builder()
                .studioNo(studioNo)
                .studioName(studioName)
                .employeesCount(employeesCount)
                .businessHours(businessHours)
                .address(address)
                .build();
    }
}