package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.application.port.in.ApproveRequestUseCase;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
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

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ApproveRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApproveRequestControllerTest extends AbstractSecurityMockSetup {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApproveRequestUseCase approveRequestUseCase;

    @Test
    @DisplayName("승인요청을 수락한다.")
    public void approveRequest() throws Exception {
        // given
        String expertNo = "expertNo";

        // when then
        mockMvc.perform(
                        patch("/api/v1/experts/{expert_no}/approval-requests/approval",
                                expertNo)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        then(approveRequestUseCase).should()
                .approveRequest(expertNo);
    }

}