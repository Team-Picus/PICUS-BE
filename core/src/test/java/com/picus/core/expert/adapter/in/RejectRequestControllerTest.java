package com.picus.core.expert.adapter.in;

import com.picus.core.expert.application.port.in.RejectRequestUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(controllers = RejectRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RejectRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RejectRequestUseCase rejectRequestUseCase;

    @Test
    @DisplayName("승인요청을 거절한다.")
    public void rejectRequest() throws Exception {
        // given
        String expertNo = "expertNo";

        // when then
        mockMvc.perform(
                        patch("/api/v1/experts/{expert_no}/approval-requests/rejection",
                                expertNo)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        then(rejectRequestUseCase).should()
                .rejectRequest(expertNo);
    }

}