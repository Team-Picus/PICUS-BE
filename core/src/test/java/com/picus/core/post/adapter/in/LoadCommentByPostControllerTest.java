package com.picus.core.post.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.adapter.in.web.data.response.LoadCommentByPostResponse;
import com.picus.core.post.adapter.in.web.mapper.LoadCommentByPostWebMapper;
import com.picus.core.post.application.port.in.LoadCommentByPostUseCase;
import com.picus.core.post.application.port.in.result.LoadCommentByPostResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoadCommentByPostController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoadCommentByPostControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    LoadCommentByPostUseCase loadCommentByPostUseCase;
    @MockitoBean
    LoadCommentByPostWebMapper webMapper;

    @Test
    @DisplayName("특정 게시물의 댓글 조회")
    public void load() throws Exception {
        // given
        String postNo = "post-123";

        LoadCommentByPostResult result = mock(LoadCommentByPostResult.class);
        List<LoadCommentByPostResult> mockResults = List.of(result);
        given(loadCommentByPostUseCase.load(postNo)).willReturn(mockResults);

        LoadCommentByPostResponse mockResponse = createMockResponse();
        given(webMapper.toResponse(mockResults)).willReturn(mockResponse);

        // when // then - 응답 값 검증
        mockMvc.perform(get("/api/v1/posts/{postNo}/comments", postNo))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.comments").exists())
                .andExpect(jsonPath("$.result.comments[0].commentNo").exists())
                .andExpect(jsonPath("$.result.comments[0].authorNo").exists())
                .andExpect(jsonPath("$.result.comments[0].authorNickname").exists())
                .andExpect(jsonPath("$.result.comments[0].authorProfileImageUrl").exists())
                .andExpect(jsonPath("$.result.comments[0].content").exists())
                .andExpect(jsonPath("$.result.comments[0].createdAt").exists());

        // then - 메서드 호출 검증
        then(loadCommentByPostUseCase).should().load(postNo);
        then(webMapper).should().toResponse(mockResults);
    }

    private LoadCommentByPostResponse createMockResponse() {
        return LoadCommentByPostResponse.builder()
                .comments(
                        List.of(LoadCommentByPostResponse.CommentResponse.builder()
                                .commentNo("")
                                .authorNo("")
                                .authorNickname("")
                                .authorProfileImageUrl("")
                                .content("")
                                .createdAt(LocalDateTime.of(2000, 1, 1, 1, 1))
                                .build())
                ).build();
    }

}