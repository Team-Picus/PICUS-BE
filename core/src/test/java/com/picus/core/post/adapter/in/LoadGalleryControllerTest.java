package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.response.LoadGalleryWebResp;
import com.picus.core.post.adapter.in.web.mapper.LoadGalleryWebMapper;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.application.port.in.LoadGalleryUseCase;
import com.picus.core.post.application.port.in.response.LoadGalleryResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoadGalleryController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoadGalleryControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoadGalleryUseCase loadGalleryUseCase;
    @MockitoBean
    private LoadGalleryWebMapper webMapper;

    @Test
    @DisplayName("갤러리 조회 요청 - 갤러리 설정이 된 경우(고정처리한 게시물이 있는 경우)")
    public void loadGallery_ifGallerySet() throws Exception {
        // given
        String expertNo = "expert-123";

        LoadGalleryResult appResp = mock(LoadGalleryResult.class);
        given(loadGalleryUseCase.load(expertNo)).willReturn(Optional.of(appResp));
        LoadGalleryWebResp webResp = LoadGalleryWebResp.builder()
                .postNo("")
                .thumbnailUrl("")
                .title("")
                .oneLineDescription("")
                .build();
        given(webMapper.toWebResp(appResp)).willReturn(webResp);

        // when
        mockMvc.perform(
                        get("/api/v1/experts/posts/{expert_no}/gallery",
                                expertNo)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.postNo").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.oneLineDescription").exists())
                .andExpect(jsonPath("$.result.thumbnailUrl").exists());
        // then

        then(loadGalleryUseCase).should().load(expertNo);
        then(webMapper).should().toWebResp(appResp);
    }

    @Test
    @DisplayName("갤러리 조회 요청 - 갤러리 설정이 안된 경우(고정처리한 게시물이 없는 경우)")
    public void loadGallery_ifGalleryNotSet() throws Exception {
        // given
        String expertNo = "expert-123";

        given(loadGalleryUseCase.load(expertNo)).willReturn(Optional.empty());

        // when
        mockMvc.perform(
                        get("/api/v1/experts/posts/{expert_no}/gallery",
                                expertNo)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").value("아직 갤러리가 설정되지 않았습니다."));
        // then
        then(loadGalleryUseCase).should().load(expertNo);
    }

}