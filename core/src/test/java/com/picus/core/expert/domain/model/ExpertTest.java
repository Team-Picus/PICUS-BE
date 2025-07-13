package com.picus.core.expert.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ExpertTest {

    @Test
    @DisplayName("ActivityDuration은 생성된지 1개월 미만이면 일단위로 설정된다.")
    public void calculateActivityDuration_lessThanOneMonth() throws Exception {
        // given
        Expert expert = Expert.builder()
                .createdAt(LocalDateTime.of(2025, 6, 14, 15, 33))
                .build();

        // when
        String activityDuration = expert.calculateActivityDuration(LocalDate.of(2025, 7, 13));

        // then
        assertThat(activityDuration).isEqualTo("29일");
    }

    @Test
    @DisplayName("ActivityDuration은 생성된지 1개월 이상이면 월단위로 설정된다.")
    public void calculateActivityDuration_moreThanOneMonth() throws Exception {
        // given
        Expert expert = Expert.builder()
                .createdAt(LocalDateTime.of(2025, 6, 13, 15, 33))
                .build();

        // when
        String activityDuration = expert.calculateActivityDuration(LocalDate.of(2025, 7, 13));

        // then
        assertThat(activityDuration).isEqualTo("1개월");
    }

    @Test
    @DisplayName("ActivityDuration은 생성된지 1년 이상이면 년단위로 설정된다.")
    public void calculateActivityDuration_moreThanOneYear() throws Exception {
        // given
        Expert expert = Expert.builder()
                .createdAt(LocalDateTime.of(2024, 7, 13, 15, 33))
                .build();

        // when
        String activityDuration = expert.calculateActivityDuration(LocalDate.of(2025, 7, 13));

        // then
        assertThat(activityDuration).isEqualTo("1년");
    }

}