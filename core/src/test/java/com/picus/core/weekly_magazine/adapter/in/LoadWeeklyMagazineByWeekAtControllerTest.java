package com.picus.core.weekly_magazine.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazineByWeekAtResponse;
import com.picus.core.weekly_magazine.adapter.in.web.mapper.LoadWeeklyMagazineByWeekAtWebMapper;
import com.picus.core.weekly_magazine.application.port.in.LoadWeeklyMagazineByWeekAtUseCase;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazineByWeekAtResult;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = LoadWeeklyMagazineByWeekAtController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoadWeeklyMagazineByWeekAtControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoadWeeklyMagazineByWeekAtUseCase useCase;
    @MockitoBean
    private LoadWeeklyMagazineByWeekAtWebMapper webMapper;

    @Test
    @DisplayName("특정 주차 주간 매거진 조회 요청")
    public void load() throws Exception {
        // given
        int year = 2025;
        int month = 10;
        int week = 2;

        WeekAt weekAt = WeekAt.builder()
                .year(year)
                .month(month)
                .week(week)
                .build();
        LoadWeeklyMagazineByWeekAtResult mockResult = mock(LoadWeeklyMagazineByWeekAtResult.class);
        given(useCase.load(weekAt)).willReturn(mockResult);
        LoadWeeklyMagazineByWeekAtResponse mockResponse = createMockResponse(weekAt);
        given(webMapper.toResponse(mockResult)).willReturn(mockResponse);

        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.topic").exists())
                .andExpect(jsonPath("$.result.topicDescription").exists())
                .andExpect(jsonPath("$.result.weekAt").exists())
                .andExpect(jsonPath("$.result.weekAt.year").exists())
                .andExpect(jsonPath("$.result.weekAt.month").exists())
                .andExpect(jsonPath("$.result.weekAt.week").exists())
                .andExpect(jsonPath("$.result.thumbnailUrl").exists());

        // then - 메서드 호출 검증
        then(useCase).should().load(weekAt);
        then(webMapper).should().toResponse(mockResult);
    }

    @Test
    @DisplayName("특정 주차 주간 매거진 조회 요청시에 year값이 2000~2100사이가 아니면 에러 발생")
    public void load_wrongYear() throws Exception {
        // given
        int year = 2101;
        int month = 10;
        int week = 2;


        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("특정 주차 주간 매거진 조회 요청시에 month값이 1~12사이가 아니면 에러 발생")
    public void load_wrongMonth() throws Exception {
        // given
        int year = 2100;
        int month = 13;
        int week = 6;


        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("특정 주차 주간 매거진 조회 요청시에 week값이 1~6사이가 아니면 에러 발생")
    public void load_wrongWeek() throws Exception {
        // given
        int year = 2100;
        int month = 12;
        int week = 7;


        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    private LoadWeeklyMagazineByWeekAtResponse createMockResponse(WeekAt weekAt) {
        return LoadWeeklyMagazineByWeekAtResponse.builder()
                .topic("")
                .topicDescription("")
                .weekAt(weekAt)
                .thumbnailUrl("")
                .build();
    }
}