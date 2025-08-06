package com.picus.core.weekly_magazine.application.service;

import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class LoadWeeklyMagazineByWeekAtServiceTest {

    @Mock
    private WeeklyMagazineReadPort weeklyMagazineReadPort;

    @InjectMocks
    private LoadWeeklyMagazineByWeekAtService service;

    @Test
    @DisplayName("특정 주차의 주간 매거진 정보를 조회한다.")
    public void load() throws Exception {
        // given
        WeekAt weekAt = WeekAt.builder()
                .year(2025)
                .month(6)
                .week(2)
                .build();

        WeeklyMagazine mockWeeklyMagazine = mock(WeeklyMagazine.class);
        given(weeklyMagazineReadPort.findByWeekAt(
                weekAt.getYear(), weekAt.getMonth(), weekAt.getWeek()
        )).willReturn(Optional.ofNullable(mockWeeklyMagazine));

        // when
        WeeklyMagazine weeklyMagazine = service.load(weekAt);

        // then
        assertThat(weeklyMagazine).isEqualTo(mockWeeklyMagazine);

        then(weeklyMagazineReadPort).should().findByWeekAt(
                weekAt.getYear(), weekAt.getMonth(), weekAt.getWeek()
        );
    }

}