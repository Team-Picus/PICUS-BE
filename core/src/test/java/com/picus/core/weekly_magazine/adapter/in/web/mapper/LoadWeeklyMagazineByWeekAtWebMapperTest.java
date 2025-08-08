package com.picus.core.weekly_magazine.adapter.in.web.mapper;

import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazineByWeekAtResponse;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazineByWeekAtResult;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoadWeeklyMagazineByWeekAtWebMapperTest {

    private LoadWeeklyMagazineByWeekAtWebMapper webMapper = new LoadWeeklyMagazineByWeekAtWebMapper();

    @Test
    @DisplayName("LoadWeeklyMagazineByWeekAtResult -> LoadWeeklyMagazineByWeekAtResponse 변환")
    void toResponse() {
        // given
        WeekAt weekAt = WeekAt.builder()
                .year(2025)
                .month(8)
                .week(2)
                .build();

        LoadWeeklyMagazineByWeekAtResult result = LoadWeeklyMagazineByWeekAtResult.builder()
                .topic("여름 여행")
                .topicDescription("무더운 여름, 시원한 여행지를 소개합니다.")
                .weekAt(weekAt)
                .thumbnailUrl("https://cdn.example.com/thumb.jpg")
                .build();

        // when
        LoadWeeklyMagazineByWeekAtResponse response = webMapper.toResponse(result);

        // then
        assertThat(response)
                .extracting(
                        LoadWeeklyMagazineByWeekAtResponse::topic,
                        LoadWeeklyMagazineByWeekAtResponse::topicDescription,
                        LoadWeeklyMagazineByWeekAtResponse::weekAt,
                        LoadWeeklyMagazineByWeekAtResponse::thumbnailUrl
                )
                .containsExactly(
                        "여름 여행",
                        "무더운 여름, 시원한 여행지를 소개합니다.",
                        weekAt,
                        "https://cdn.example.com/thumb.jpg"
                );
    }

}