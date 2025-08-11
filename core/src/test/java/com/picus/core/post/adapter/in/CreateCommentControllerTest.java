package com.picus.core.post.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.adapter.in.web.data.request.CreateCommentRequest;
import com.picus.core.post.adapter.in.web.mapper.CreateCommentWebMapper;
import com.picus.core.post.application.port.in.CreateCommentUseCase;
import com.picus.core.post.application.port.in.command.CreateCommentCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CreateCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreateCommentControllerTest extends AbstractSecurityMockSetup {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateCommentUseCase createCommentUseCase;
    @MockitoBean
    CreateCommentWebMapper webMapper;

    @Test
    @DisplayName("댓글 저장 요청한다.")
    public void create() throws Exception {
        // given
        String postNo = "postNo";
        CreateCommentRequest request = CreateCommentRequest.builder()
                .content("content")
                .build();

        CreateCommentCommand mockCommand = mock(CreateCommentCommand.class);
        given(webMapper.toCommand(postNo, TEST_USER_ID, request)).willReturn(mockCommand);

        // when
        mockMvc.perform(
                        post("/api/v1/posts/{postNo}/comments", postNo)
                                .contentType("application/json;charset=UTF-8")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        // then
        then(webMapper).should().toCommand(postNo, TEST_USER_ID, request);
        then(createCommentUseCase).should().create(any(CreateCommentCommand.class));
    }

}