package com.picus.core.expert.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudioTest {

    @Test
    @DisplayName("모든 필드를 업데이트한다.")
    void updateStudio_allFields() {
        // given
        Studio studio = Studio.builder()
                .studioNo("ST-001")
                .studioName("기존 이름")
                .employeesCount(2)
                .businessHours("09:00~18:00")
                .address("기존 주소")
                .build();

        // when
        studio.updateStudio("새 이름", 5, "10:00~19:00", "새 주소");

        // then
        assertThat(studio.getStudioName()).isEqualTo("새 이름");
        assertThat(studio.getEmployeesCount()).isEqualTo(5);
        assertThat(studio.getBusinessHours()).isEqualTo("10:00~19:00");
        assertThat(studio.getAddress()).isEqualTo("새 주소");
    }

    @Test
    @DisplayName("null 값은 무시되고 기존 값이 유지된다.")
    void updateStudio_partialUpdate() {
        // given
        Studio studio = Studio.builder()
                .studioNo("ST-002")
                .studioName("기존 이름")
                .employeesCount(3)
                .businessHours("09:00~18:00")
                .address("기존 주소")
                .build();

        // when
        studio.updateStudio(null, null, "변경된 시간", null);

        // then
        assertThat(studio.getStudioName()).isEqualTo("기존 이름");
        assertThat(studio.getEmployeesCount()).isEqualTo(3);
        assertThat(studio.getBusinessHours()).isEqualTo("변경된 시간");
        assertThat(studio.getAddress()).isEqualTo("기존 주소");
    }

    @Test
    @DisplayName("모든 파라미터가 null이면 아무것도 변경되지 않는다.")
    void updateStudio_allNull() {
        // given
        Studio studio = Studio.builder()
                .studioNo("ST-003")
                .studioName("초기 이름")
                .employeesCount(1)
                .businessHours("08:00~17:00")
                .address("초기 주소")
                .build();

        // when
        studio.updateStudio(null, null, null, null);

        // then
        assertThat(studio.getStudioName()).isEqualTo("초기 이름");
        assertThat(studio.getEmployeesCount()).isEqualTo(1);
        assertThat(studio.getBusinessHours()).isEqualTo("08:00~17:00");
        assertThat(studio.getAddress()).isEqualTo("초기 주소");
    }
}