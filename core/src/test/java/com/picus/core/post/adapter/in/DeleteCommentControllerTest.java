package com.picus.core.post.adapter.in;

import com.picus.core.shared.ControllerTestSupport;
import com.picus.core.post.application.port.in.DeleteCommentUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(controllers = DeleteCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class DeleteCommentControllerTest extends ControllerTestSupport {
    @Autowired
    MockMvc mockMvc;
    
    @MockitoBean
    DeleteCommentUseCase deleteCommentUseCase;
    
    @Test
    @DisplayName("댓글을 삭제한다.")
    public void delete_success() throws Exception {
        // given
        String commentNo = "cmt-123";

        // when // then
        mockMvc.perform(
                        delete("/api/v1/comments/{comment_no}", commentNo)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        // then - usecase 호출 검증
        then(deleteCommentUseCase).should().delete(commentNo, TEST_USER_ID);
    }
    

}