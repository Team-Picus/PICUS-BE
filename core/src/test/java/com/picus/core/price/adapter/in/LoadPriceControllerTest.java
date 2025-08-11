package com.picus.core.price.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse;
import com.picus.core.price.adapter.in.web.mapper.LoadPriceWebMapper;
import com.picus.core.price.application.port.in.LoadPriceUseCase;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
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

import java.util.List;

import static com.picus.core.price.domain.vo.PriceThemeType.BEAUTY;
import static com.picus.core.price.domain.vo.PriceThemeType.SNAP;
import static com.picus.core.price.domain.vo.SnapSubTheme.ADMISSION;
import static com.picus.core.price.domain.vo.SnapSubTheme.FAMILY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(controllers = LoadPriceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class LoadPriceControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LoadPriceUseCase loadPriceUseCase;
    @MockitoBean
    LoadPriceWebMapper webMapper;

    @Test
    @DisplayName("특정 전문가의 가격 정보를 조회한다.")
    public void getPricesByExpert_success() throws Exception {
        // given
        String expertNo = "expert_no1";

        LoadPriceResponse mockResponse = createMockResponse();
        given(webMapper.toResponse(any())).willReturn(mockResponse);

        // when // then
        mockMvc.perform(
                        get("/api/v1/experts/{expert_no}/prices",
                                expertNo)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceThemeType").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].snapSubTheme").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].priceRefImageNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].fileKey").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].imageUrl").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].imageOrder").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].packageNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].price").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].contents").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].notice").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].optionNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].count").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].price").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].contents").isArray());

        then(loadPriceUseCase).should().loadByExpertNo(expertNo,null, null);
    }

    @Test
    @DisplayName("특정 전문가의 가격 정보를 조회한다. - postThemeTypes, snapSubThemes이 조건으로 들어가 있는 경우")
    public void getPricesByExpert_success_postThemeTypes() throws Exception {
        // given
        String expertNo = "expert_no1";

        LoadPriceResponse mockResponse = createMockResponse();
        given(webMapper.toResponse(any())).willReturn(mockResponse);

        String[] priceThemeTypes = {"SNAP", "BEAUTY"};
        String snapSubTheme = "FAMILY";

        // when // then
        mockMvc.perform(
                        get("/api/v1/experts/{expert_no}/prices",
                                expertNo)
                                .param("priceThemeTypes", priceThemeTypes)
                                .param("snapSubThemes", snapSubTheme)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceThemeType").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].snapSubTheme").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].priceRefImageNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].fileKey").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].imageUrl").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].priceReferenceImages[0].imageOrder").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].packageNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].price").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].contents").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].packages[0].notice").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].optionNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].count").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].price").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.prices[0].options[0].contents").isArray());

        // then
        then(loadPriceUseCase).should().loadByExpertNo(expertNo, List.of(SNAP, BEAUTY), List.of(FAMILY));
    }

    @Test
    @DisplayName("잘못된 postThemeTypes이 있는 경우 에러가 발생한다.")
    public void getPricesByExpert_wrong_postThemeTypes() throws Exception {
        // given
        String expertNo = "expert_no1";

        LoadPriceResponse mockResponse = createMockResponse();
        given(webMapper.toResponse(any())).willReturn(mockResponse);

        String[] priceThemeTypes = {"SNAPXXX"};

        // when // then
        mockMvc.perform(
                        get("/api/v1/experts/{expert_no}/prices",
                                expertNo)
                                .param("priceThemeTypes", priceThemeTypes)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    @DisplayName("잘못된 snapSubThemes가 있는 경우 에러가 발생한다.")
    public void getPricesByExpert_wrong_snapSubThemes() throws Exception {
        // given
        String expertNo = "expert_no1";

        LoadPriceResponse mockResponse = createMockResponse();
        given(webMapper.toResponse(any())).willReturn(mockResponse);

        String[] priceThemeTypes = {"SNAP"};
        String[] snapSubThemes = {"FAMILYXX"};

        // when // then
        mockMvc.perform(
                        get("/api/v1/experts/{expert_no}/prices",
                                expertNo)
                                .param("priceThemeTypes", priceThemeTypes)
                                .param("snapSubThemes", snapSubThemes)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private LoadPriceResponse createMockResponse() {
        return LoadPriceResponse.builder()
                .prices(List.of(
                        LoadPriceResponse.PriceResponse.builder()
                                .priceNo("")
                                .priceThemeType(SNAP)
                                .snapSubTheme(ADMISSION)
                                .priceReferenceImages(List.of(
                                        LoadPriceResponse.PriceResponse.PriceReferenceImageResponse.builder()
                                                .priceRefImageNo("")
                                                .fileKey("")
                                                .imageUrl("")
                                                .imageOrder(0)
                                                .build()
                                ))
                                .packages(List.of(
                                        LoadPriceResponse.PriceResponse.PackageResponse.builder()
                                                .packageNo("")
                                                .name("")
                                                .price(0)
                                                .contents(List.of())
                                                .notice("")
                                                .build()
                                ))
                                .options(List.of(
                                        LoadPriceResponse.PriceResponse.OptionResponse.builder()
                                                .optionNo("")
                                                .name("")
                                                .count(0)
                                                .price(0)
                                                .contents(List.of(""))
                                                .build()
                                ))
                                .build()
                ))
                .build();
    }
}