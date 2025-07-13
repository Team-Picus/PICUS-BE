package com.picus.core.expert.adapter.in;

import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RequestApprovalControllerTest.class)
class RequestApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RequestApprovalUseCase requestApprovalUseCase;

    @Test
    @DisplayName("")
    public void requestApproval() throws Exception {
        // given

        // when

        // then
    }

}