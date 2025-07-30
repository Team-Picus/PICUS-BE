package com.picus.core.expert.adapter.in;

import com.picus.core.expert.application.port.in.RejectRequestUseCase;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RejectRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RejectRequestControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RejectRequestUseCase rejectRequestUseCase;


    @Test
    @DisplayName("승인요청을 거절한다.")
    public void rejectRequest() throws Exception {
        // given
        String expertNo = "expertNo";

        mockMvc.perform(
                        patch("/api/v1/experts/{expert_no}/approval-requests/rejection", expertNo)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}