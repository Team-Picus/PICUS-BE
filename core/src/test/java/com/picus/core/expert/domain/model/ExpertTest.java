package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
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
}