package com.picus.core.price.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.price.adapter.in.UpdatePriceController;
import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.adapter.in.web.mapper.UpdatePriceWebMapper;
import com.picus.core.price.application.port.in.PriceInfoCommand;
import com.picus.core.price.application.port.in.request.UpdatePriceListAppReq;
import com.picus.core.price.application.port.in.request.ChangeStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;


@WebMvcTest(controllers = UpdatePriceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UpdatePriceControllerTest extends AbstractSecurityMockSetup {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    PriceInfoCommand priceInfoCommand;
    @MockitoBean
    UpdatePriceWebMapper webMapper;

    @Test
    @DisplayName("가격정보 변경 Controller는 입력을 받아 매핑 후 Usecase에 값을 넘겨준다.")
    public void applyPriceChanges_success() throws Exception {
        // given
        UpdatePriceReferenceImageWebReq imageWebRequest =
                createPriceRefImageWebRequest("img-001",
                        "file-key-001", 1, ChangeStatus.NEW);

        UpdatePackageWebReq updatePackageWebReq =
                createPackageWebRequest("pkg-001", "패키지A", 10000,
                        List.of("내용1", "내용2"), "주의사항", ChangeStatus.NEW);

        UpdateOptionWebReq updateOptionWebReq =
                createOptionWebRequest("opt-001", "옵션A", 2,
                        3000, List.of("옵션내용"), ChangeStatus.NEW);

        UpdatePriceWebReq updatePriceWebRequest =
                createPriceWebRequest("price-001", "THEME", ChangeStatus.NEW,
                        imageWebRequest, updatePackageWebReq, updateOptionWebReq);

        UpdatePriceListWebReq webRequest = new UpdatePriceListWebReq(List.of(updatePriceWebRequest));

        given(webMapper.toCommand(webRequest))
                .willReturn(Mockito.mock(UpdatePriceListAppReq.class));

        // when
        mockMvc.perform(
                        patch("/api/v1/experts/prices")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        // then
        then(webMapper).should()
                .toCommand(webRequest);
        then(priceInfoCommand).should()
                .update(any(UpdatePriceListAppReq.class), eq(TEST_USER_ID));
    }

    @Test
    @DisplayName("가격정보 변경 Controller는 입력값이 없으면 에러가 발생한다.")
    public void applyPriceChanges_prices_null() throws Exception {
        // when then
        mockMvc.perform(
                        patch("/api/v1/experts/prices")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @DisplayName("가격정보 변경 Controller 입력값 PriceWebRequest의 ChangeStatus가 빠지면 에러가 발생한다.")
    public void applyPriceChanges_PriceWebRequest_change_status_null() throws Exception {
        // given

        UpdatePriceWebReq updatePriceWebRequest =
                createPriceWebRequest("price-001", "THEME", null);

        UpdatePriceListWebReq webRequest = new UpdatePriceListWebReq(List.of(updatePriceWebRequest));

        given(webMapper.toCommand(webRequest))
                .willReturn(Mockito.mock(UpdatePriceListAppReq.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/prices")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("가격정보 변경 Controller 입력값 PriceWebRequest안에 PriceReferenceImageWebRequest의" +
            " ChangeStatus가 빠지면 에러가 발생한다.")
    public void applyPriceChanges_PriceReferenceImageWebRequest_change_status_null() throws Exception {
        // given
        UpdatePriceReferenceImageWebReq imageWebRequest =
                createPriceRefImageWebRequest("img-001",
                        "file-key-001", 1, null);

        UpdatePackageWebReq updatePackageWebReq =
                createPackageWebRequest("pkg-001", "패키지A", 10000,
                        List.of("내용1", "내용2"), "주의사항", ChangeStatus.NEW);

        UpdateOptionWebReq updateOptionWebReq =
                createOptionWebRequest("opt-001", "옵션A", 2,
                        3000, List.of("옵션내용"), ChangeStatus.NEW);

        UpdatePriceWebReq updatePriceWebRequest =
                createPriceWebRequest("price-001", "THEME", ChangeStatus.NEW,
                        imageWebRequest, updatePackageWebReq, updateOptionWebReq);

        UpdatePriceListWebReq webRequest = new UpdatePriceListWebReq(List.of(updatePriceWebRequest));

        given(webMapper.toCommand(webRequest))
                .willReturn(Mockito.mock(UpdatePriceListAppReq.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/prices")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    private UpdatePriceReferenceImageWebReq createPriceRefImageWebRequest(String priceRefImageNo, String fileKey,
                                                                          int imageOrder, ChangeStatus changeStatus) {
        return UpdatePriceReferenceImageWebReq.builder()
                .priceRefImageNo(priceRefImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .status(changeStatus)
                .build();
    }

    private UpdatePackageWebReq createPackageWebRequest(String packageNo, String name, int price, List<String> contents,
                                                        String notice, ChangeStatus changeStatus) {
        return UpdatePackageWebReq.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .status(changeStatus)
                .build();
    }

    private UpdateOptionWebReq createOptionWebRequest(String optionNo, String name, int count, int price,
                                                      List<String> contents, ChangeStatus changeStatus) {
        UpdateOptionWebReq updateOptionWebReq = UpdateOptionWebReq.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .status(changeStatus)
                .build();
        return updateOptionWebReq;
    }

    private UpdatePriceWebReq createPriceWebRequest(String priceNo, String theme, ChangeStatus changeStatus,
                                                    UpdatePriceReferenceImageWebReq imageWebRequest,
                                                    UpdatePackageWebReq updatePackageWebReq, UpdateOptionWebReq updateOptionWebReq) {
        return UpdatePriceWebReq.builder()
                .priceNo(priceNo)
                .priceThemeType(theme)
                .priceReferenceImages(List.of(imageWebRequest))
                .packages(List.of(updatePackageWebReq))
                .options(List.of(updateOptionWebReq))
                .status(changeStatus)
                .build();
    }

    private UpdatePriceWebReq createPriceWebRequest(String priceNo, String theme, ChangeStatus changeStatus) {
        return UpdatePriceWebReq.builder()
                .priceNo(priceNo)
                .priceThemeType(theme)
                .status(changeStatus)
                .build();
    }


}