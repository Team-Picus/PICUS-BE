package com.picus.core.post.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.application.port.in.DeletePostUseCase;
import com.picus.core.price.adapter.in.LoadPriceController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(controllers = DeletePostController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class DeletePostControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    DeletePostUseCase deletePostUseCase;

    @Test
    @DisplayName("게시물을 삭제한다.")
    public void delete_success() throws Exception {
        // given
        String postNo = "post-123";
        String userNo = TEST_USER_ID;

        // when // then
        mockMvc.perform(
                delete("/api/v1/posts/{post_no}", postNo)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        then(deletePostUseCase).should().delete(postNo, userNo);
    }
}