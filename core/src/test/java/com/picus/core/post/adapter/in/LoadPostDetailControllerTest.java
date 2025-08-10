package com.picus.core.post.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.adapter.in.web.data.response.LoadPostDetailResponse;
import com.picus.core.post.adapter.in.web.mapper.LoadPostDetailWebMapper;
import com.picus.core.post.application.port.in.LoadPostDetailUseCase;
import com.picus.core.post.application.port.in.result.LoadPostDetailResult;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoadPostDetailController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoadPostDetailControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    LoadPostDetailUseCase loadPostDetailUseCase;
    @MockitoBean
    LoadPostDetailWebMapper webMapper;

    @Test
    @DisplayName("특정 게시물을 조회한다.")
    public void load() throws Exception {
        // given
        String postNo = "post-123";

        LoadPostDetailResult result = mock(LoadPostDetailResult.class);
        LoadPostDetailResponse response = createDefaultResponse();

        // given - 의존하는 빈의 메서드 스텁
        stub(postNo, result, response);

        // when // then - API 응답 검증
        mockMvc.perform(get("/api/v1/posts/{postNo}", postNo))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.postNo").exists())
                .andExpect(jsonPath("$.result.images").exists())
                .andExpect(jsonPath("$.result.images[0].imageNo").exists())
                .andExpect(jsonPath("$.result.images[0].fileKey").exists())
                .andExpect(jsonPath("$.result.images[0].imageUrl").exists())
                .andExpect(jsonPath("$.result.images[0].imageOrder").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.oneLineDescription").exists())
                .andExpect(jsonPath("$.result.detailedDescription").exists())
                .andExpect(jsonPath("$.result.themeTypes").exists())
                .andExpect(jsonPath("$.result.snapSubThemes").exists())
                .andExpect(jsonPath("$.result.moodTypes").exists())
                .andExpect(jsonPath("$.result.spaceType").exists())
                .andExpect(jsonPath("$.result.spaceAddress").exists())
                .andExpect(jsonPath("$.result.packageNos").exists())
                .andExpect(jsonPath("$.result.packageNos[0]").exists())
                .andExpect(jsonPath("$.result.updatedAt").exists())
                .andExpect(jsonPath("$.result.authorInfo").exists())
                .andExpect(jsonPath("$.result.authorInfo.expertNo").exists())
                .andExpect(jsonPath("$.result.authorInfo.nickname").exists());

        // then - 의존하는 빈 메서드 호출 검증
        then(loadPostDetailUseCase).should().load(postNo);
        then(webMapper).should().toResponse(result);
    }

    private LoadPostDetailResponse createDefaultResponse() {
        return LoadPostDetailResponse.builder()
                .postNo("post-123")
                .images(List.of(LoadPostDetailResponse.PostImageResponse.builder()
                        .imageNo("img-001")
                        .fileKey("file-key-001")
                        .imageUrl("https://cdn.picus.com/test.jpg")
                        .imageOrder(0)
                        .build()))
                .title("제목")
                .oneLineDescription("요약")
                .detailedDescription("상세 설명")
                .themeTypes(List.of())
                .snapSubThemes(List.of())
                .moodTypes(List.of())
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("주소")
                .packageNos(List.of("pkg-001"))
                .updatedAt(LocalDateTime.now())
                .authorInfo(LoadPostDetailResponse.AuthorInfo.builder()
                        .expertNo("exp-123")
                        .nickname("작성자")
                        .build())
                .build();
    }

    private void stub(String postNo, LoadPostDetailResult result, LoadPostDetailResponse response) {
        given(loadPostDetailUseCase.load(postNo)).willReturn(result);
        given(webMapper.toResponse(result)).willReturn(response);
    }

}