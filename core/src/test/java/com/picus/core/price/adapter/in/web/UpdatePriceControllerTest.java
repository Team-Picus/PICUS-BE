package com.picus.core.price.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.price.adapter.in.UpdatePriceController;
import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.adapter.in.web.mapper.UpdatePriceWebMapper;
import com.picus.core.price.application.port.in.UpdatePriceUseCase;
import com.picus.core.price.application.port.in.command.UpdatePriceListCommand;
import com.picus.core.price.application.port.in.command.ChangeStatus;
import com.picus.core.price.domain.vo.PriceThemeType;
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
    UpdatePriceUseCase updatePriceUseCase;
    @MockitoBean
    UpdatePriceWebMapper webMapper;

    @Test
    @DisplayName("가격정보 변경 Controller는 입력을 받아 매핑 후 Usecase에 값을 넘겨준다.")
    public void applyPriceChanges_success() throws Exception {
        // given
        UpdatePriceReferenceImageRequest imageWebRequest =
                createPriceRefImageWebRequest("img-001",
                        "file-key-001", 1, ChangeStatus.NEW);

        UpdatePackageRequest updatePackageRequest =
                createPackageWebRequest("pkg-001", "패키지A", 10000,
                        List.of("내용1", "내용2"), "주의사항", ChangeStatus.NEW);

        UpdateOptionRequest updateOptionRequest =
                createOptionWebRequest("opt-001", "옵션A", 2,
                        3000, List.of("옵션내용"), ChangeStatus.NEW);

        UpdatePriceRequest updatePriceWebRequest =
                createPriceWebRequest("price-001", PriceThemeType.FASHION, ChangeStatus.NEW,
                        imageWebRequest, updatePackageRequest, updateOptionRequest);

        UpdatePriceListRequest webRequest = new UpdatePriceListRequest(List.of(updatePriceWebRequest));

        given(webMapper.toCommand(webRequest))
                .willReturn(Mockito.mock(UpdatePriceListCommand.class));

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
        then(updatePriceUseCase).should()
                .update(any(UpdatePriceListCommand.class), eq(TEST_USER_ID));
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

        UpdatePriceRequest updatePriceWebRequest =
                createPriceWebRequest("price-001", PriceThemeType.FASHION, null);

        UpdatePriceListRequest webRequest = new UpdatePriceListRequest(List.of(updatePriceWebRequest));

        given(webMapper.toCommand(webRequest))
                .willReturn(Mockito.mock(UpdatePriceListCommand.class));

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
    @DisplayName("가격정보 변경 Controller 입력값 PriceWebRequest의 priceThemeType이 빠지면 에러가 발생한다.")
    public void applyPriceChanges_PriceWebRequest_priceThemeType_null() throws Exception {
        // given

        UpdatePriceRequest updatePriceWebRequest =
                createPriceWebRequest("price-001", null, ChangeStatus.UPDATE);

        UpdatePriceListRequest webRequest = new UpdatePriceListRequest(List.of(updatePriceWebRequest));

        given(webMapper.toCommand(webRequest))
                .willReturn(Mockito.mock(UpdatePriceListCommand.class));

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
        UpdatePriceReferenceImageRequest imageWebRequest =
                createPriceRefImageWebRequest("img-001",
                        "file-key-001", 1, null);

        UpdatePackageRequest updatePackageRequest =
                createPackageWebRequest("pkg-001", "패키지A", 10000,
                        List.of("내용1", "내용2"), "주의사항", ChangeStatus.NEW);

        UpdateOptionRequest updateOptionRequest =
                createOptionWebRequest("opt-001", "옵션A", 2,
                        3000, List.of("옵션내용"), ChangeStatus.NEW);

        UpdatePriceRequest updatePriceWebRequest =
                createPriceWebRequest("price-001", PriceThemeType.FASHION, ChangeStatus.NEW,
                        imageWebRequest, updatePackageRequest, updateOptionRequest);

        UpdatePriceListRequest webRequest = new UpdatePriceListRequest(List.of(updatePriceWebRequest));

        given(webMapper.toCommand(webRequest))
                .willReturn(Mockito.mock(UpdatePriceListCommand.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/prices")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    private UpdatePriceReferenceImageRequest createPriceRefImageWebRequest(String priceRefImageNo, String fileKey,
                                                                           int imageOrder, ChangeStatus changeStatus) {
        return UpdatePriceReferenceImageRequest.builder()
                .priceRefImageNo(priceRefImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .status(changeStatus)
                .build();
    }

    private UpdatePackageRequest createPackageWebRequest(String packageNo, String name, int price, List<String> contents,
                                                         String notice, ChangeStatus changeStatus) {
        return UpdatePackageRequest.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .status(changeStatus)
                .build();
    }

    private UpdateOptionRequest createOptionWebRequest(String optionNo, String name, int count, int price,
                                                       List<String> contents, ChangeStatus changeStatus) {
        UpdateOptionRequest updateOptionRequest = UpdateOptionRequest.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .status(changeStatus)
                .build();
        return updateOptionRequest;
    }

    private UpdatePriceRequest createPriceWebRequest(String priceNo, PriceThemeType priceThemeType, ChangeStatus changeStatus,
                                                     UpdatePriceReferenceImageRequest imageWebRequest,
                                                     UpdatePackageRequest updatePackageRequest, UpdateOptionRequest updateOptionRequest) {
        return UpdatePriceRequest.builder()
                .priceNo(priceNo)
                .priceThemeType(priceThemeType)
                .priceReferenceImages(List.of(imageWebRequest))
                .packages(List.of(updatePackageRequest))
                .options(List.of(updateOptionRequest))
                .status(changeStatus)
                .build();
    }

    private UpdatePriceRequest createPriceWebRequest(String priceNo, PriceThemeType priceThemeType, ChangeStatus changeStatus) {
        return UpdatePriceRequest.builder()
                .priceNo(priceNo)
                .priceThemeType(priceThemeType)
                .status(changeStatus)
                .build();
    }


}