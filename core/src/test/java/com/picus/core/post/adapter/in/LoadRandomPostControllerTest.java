package com.picus.core.post.adapter.in;

import com.picus.core.shared.ControllerTestSupport;
import com.picus.core.post.adapter.in.web.data.response.LoadRandomPostResponse;
import com.picus.core.post.adapter.in.web.mapper.LoadRandomPostWebMapper;
import com.picus.core.post.application.port.in.LoadRandomPostUseCase;
import com.picus.core.post.application.port.in.result.LoadRandomPostResult;
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

@WebMvcTest(controllers = LoadRandomPostController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoadRandomPostControllerTest extends ControllerTestSupport {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    LoadRandomPostUseCase loadRandomPostUseCase;
    @MockitoBean
    LoadRandomPostWebMapper webMapper;

    @Test
    @DisplayName("랜덤으로 선정된 N개의 게시물을 조회한다.")
    public void load() throws Exception {
        // given
        int size = 1;

        LoadRandomPostResult mockResult = mock(LoadRandomPostResult.class);
        given(loadRandomPostUseCase.load(size)).willReturn(List.of(mockResult));
        LoadRandomPostResponse mockResponse = LoadRandomPostResponse.builder()
                .postNo("post-123")
                .nickname("nick1")
                .thumbnailUrl("img.com")
                .build();
        given(webMapper.toResponse(mockResult)).willReturn(mockResponse);

        // when // then
        mockMvc.perform(get("/api/v1/posts/random")
                        .param("size", String.valueOf(size))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].postNo").exists())
                .andExpect(jsonPath("$.result[0].nickname").exists())
                .andExpect(jsonPath("$.result[0].thumbnailUrl").exists());

        then(loadRandomPostUseCase).should().load(size);
        then(webMapper).should().toResponse(mockResult);
    }

    @Test
    @DisplayName("랜덤으로 선정된 N개의 게시물을 조회할 때 size를 생략하면 기본 5개로 설정된다.")
    public void load_size_default5() throws Exception {
        // given
        // when // then
        mockMvc.perform(get("/api/v1/posts/random")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        then(loadRandomPostUseCase).should().load(5);
    }
}