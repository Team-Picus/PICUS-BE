package com.picus.core.weekly_magazine.domain.model.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WeekAtTest {

    @Test
    @DisplayName("WeekAt을 생성할 때 year는 2000 ~ 2100 사이여야 한다.")
    public void weekAt_year_error1() throws Exception {
        // when // then
        assertThatThrownBy(() -> WeekAt.builder()
                .year(1999)
                .month(5)
                .week(3)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("year는 2000~2100 사이여야 합니다.");
    }

    @Test
    @DisplayName("WeekAt을 생성할 때 year는 2000 ~ 2100 사이여야 한다.")
    public void weekAt_year_error2() throws Exception {
        // when // then
        assertThatThrownBy(() -> WeekAt.builder()
                .year(2101)
                .month(5)
                .week(3)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("year는 2000~2100 사이여야 합니다.");
    }

    @Test
    @DisplayName("WeekAt을 생성할 때 month는 1~12 사이여야 한다.")
    public void weekAt_month_error1() throws Exception {
        // when // then
        assertThatThrownBy(() -> WeekAt.builder()
                .year(2025)
                .month(0)
                .week(3)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("month는 1~12 사이여야 합니다.");
    }
    @Test
    @DisplayName("WeekAt을 생성할 때 month는 1~12 사이여야 한다.")
    public void weekAt_month_error2() throws Exception {
        // when // then
        assertThatThrownBy(() -> WeekAt.builder()
                .year(2025)
                .month(13)
                .week(3)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("month는 1~12 사이여야 합니다.");
    }

    @Test
    @DisplayName("WeekAt을 생성할 때 week는 1~6 사이여야 한다.")
    public void weekAt_week_error1() throws Exception {
        // when // then
        assertThatThrownBy(() -> WeekAt.builder()
                .year(2025)
                .month(12)
                .week(0)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("week는 1~6 사이여야 합니다.");
    }
    @Test
    @DisplayName("WeekAt을 생성할 때 week는 1~6 사이여야 한다.")
    public void weekAt_week_error2() throws Exception {
        // when // then
        assertThatThrownBy(() -> WeekAt.builder()
                .year(2025)
                .month(12)
                .week(7)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("week는 1~6 사이여야 합니다.");
    }
}