package com.picus.core.price.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.price.adapter.in.web.GetPricesByExpertController;
import com.picus.core.price.adapter.in.web.mapper.GetPricesByExpertWebMapper;
import com.picus.core.price.application.port.in.GetPricesByExpertQuery;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(controllers = GetPricesByExpertController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class GetPricesByExpertControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GetPricesByExpertQuery getPricesByExpertQuery;
    @MockitoBean
    GetPricesByExpertWebMapper getPricesByExpertWebMapper;

    @Test
    @DisplayName("특정 전문가의 가격 정보를 조회한다.")
    public void getPricesByExpert_success() throws Exception {
        // given
        String expertNo = "expert_no1";

        // when // then
        mockMvc.perform(
                        get("/api/v1/experts/{expert_no}/prices",
                                expertNo)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists());
    }
}