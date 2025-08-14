package com.picus.core.weekly_magazine.adapter.in;

import com.picus.core.shared.ControllerTestSupport;
import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazinePostByWeekAtResponse;
import com.picus.core.weekly_magazine.adapter.in.web.mapper.LoadWeeklyMagazinePostByWeekAtWebMapper;
import com.picus.core.weekly_magazine.application.port.in.LoadWeeklyMagazinePostByWeekAtUseCase;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazinePostByWeekAtResult;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoadWeeklyMagazinePostByWeekAtController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoadWeeklyMagazinePostByWeekAtControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    LoadWeeklyMagazinePostByWeekAtUseCase useCase;
    @MockitoBean
    LoadWeeklyMagazinePostByWeekAtWebMapper webMapper;

    @Test
    @DisplayName("특정 주차 주간 매거진 게시물 목록 조회 요청")
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

        LoadWeeklyMagazinePostByWeekAtResult mockResult = mock(LoadWeeklyMagazinePostByWeekAtResult.class);
        given(useCase.load(weekAt)).willReturn(List.of(mockResult));
        LoadWeeklyMagazinePostByWeekAtResponse mockResponse = createMockResponse(weekAt);
        given(webMapper.toResponse(List.of(mockResult))).willReturn(mockResponse);

        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}/posts",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.posts").exists())
                .andExpect(jsonPath("$.result.posts[0].postNo").exists())
                .andExpect(jsonPath("$.result.posts[0].authorNickname").exists())
                .andExpect(jsonPath("$.result.posts[0].postTitle").exists())
                .andExpect(jsonPath("$.result.posts[0].thumbnailUrl").exists());
        // then - 메서드 호출 검증
        then(useCase).should().load(weekAt);
        then(webMapper).should().toResponse(List.of(mockResult));
    }

    @Test
    @DisplayName("특정 주차 주간 매거진 게시물 목록 조회 요청시에 year값이 2000~2100사이가 아니면 에러 발생")
    public void load_wrongYear() throws Exception {
        // given
        int year = 2101;
        int month = 10;
        int week = 2;

        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}/posts",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("특정 주차 주간 매거진 게시물 목록 조회 요청시에 month값이 1~12사이가 아니면 에러 발생")
    public void load_wrongMonth() throws Exception {
        // given
        int year = 2100;
        int month = 13;
        int week = 6;

        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}/posts",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("특정 주차 주간 매거진 게시물 목록 조회 요청시에 week값이 1~6사이가 아니면 에러 발생")
    public void load_wrongWeek() throws Exception {
        // given
        int year = 2100;
        int month = 12;
        int week = 7;

        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/weekly_magazine/{year}/{month}/{week}/posts",
                        year, month, week
                ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }


    private LoadWeeklyMagazinePostByWeekAtResponse createMockResponse(WeekAt weekAt) {
        return LoadWeeklyMagazinePostByWeekAtResponse.builder()
                .posts(List.of(
                        LoadWeeklyMagazinePostByWeekAtResponse.PostResponse.builder()
                                .postNo("")
                                .authorNickname("")
                                .postTitle("")
                                .thumbnailUrl("")
                                .build()
                )).build();
    }

}