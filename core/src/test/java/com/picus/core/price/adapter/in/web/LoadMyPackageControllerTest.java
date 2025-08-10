package com.picus.core.price.adapter.in.web;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.price.adapter.in.web.data.response.LoadMyPackageResponse;
import com.picus.core.price.adapter.in.web.mapper.LoadMyPackageWebMapper;
import com.picus.core.price.application.port.in.LoadMyPackageUseCase;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
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
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoadMyPackageController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoadMyPackageControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoadMyPackageUseCase loadMyPackageUseCase;
    @MockitoBean
    private LoadMyPackageWebMapper webMapper;

    @Test
    @DisplayName("나의 패키지 정보 조회 요청")
    public void load() throws Exception {
        // given
        Price mockPrice = mock(Price.class);
        given(loadMyPackageUseCase.load(TEST_USER_ID)).willReturn(List.of(mockPrice));
        LoadMyPackageResponse mockResponse = createMockResponse();
        given(webMapper.toResponse(List.of(mockPrice))).willReturn(mockResponse);

        // when // then
        mockMvc.perform(get("/api/v1/experts/prices/packages/me"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.prices").isArray())
                .andExpect(jsonPath("$.result.prices[0].priceThemeType").exists())
                .andExpect(jsonPath("$.result.prices[0].snapSubTheme").exists())
                .andExpect(jsonPath("$.result.prices[0].packages").isArray())
                .andExpect(jsonPath("$.result.prices[0].packages[0].packageNo").exists())
                .andExpect(jsonPath("$.result.prices[0].packages[0].name").exists());
    }

    private LoadMyPackageResponse createMockResponse() {
        return LoadMyPackageResponse.builder()
                .prices(List.of(LoadMyPackageResponse.PriceResponse.builder()
                        .priceThemeType(PriceThemeType.SNAP)
                        .snapSubTheme(SnapSubTheme.ADMISSION)
                        .packages(List.of(LoadMyPackageResponse.PriceResponse.PackageResponse.builder()
                                .packageNo("")
                                .name("")
                                .build()))
                        .build()))
                .build();
    }

}