package com.picus.core.post.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.shared.ControllerTestSupport;
import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest;
import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest.PostImageRequest;
import com.picus.core.post.adapter.in.web.mapper.CreatePostWebMapper;
import com.picus.core.post.application.port.in.CreatePostUseCase;
import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CreatePostController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreatePostControllerTest extends ControllerTestSupport {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreatePostUseCase createPostUseCase;
    @MockitoBean
    private CreatePostWebMapper createPostWebMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Post 작성 요청 - 성공")
    public void write_success() throws Exception {
        // given
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(
                        PostImageRequest.builder().fileKey("img1.jpg").imageOrder(1).build(),
                        PostImageRequest.builder().fileKey("img2.jpg").imageOrder(2).build()
                ),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        String currenUserNo = TEST_USER_ID;

        CreatePostCommand createPostCommand = Mockito.mock(CreatePostCommand.class);
        given(createPostWebMapper.toCommand(webReq, currenUserNo)).willReturn(createPostCommand);

        // when // then
        mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(webReq))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        // then
        then(createPostWebMapper).should().toCommand(webReq, currenUserNo);
        then(createPostUseCase).should().create(createPostCommand);
    }

    @Test
    @DisplayName("Post 작성 요청 - postImages 비어있으면 실패")
    public void createPost_fail_ImagesEmpty() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - postImages 안에 필드가 비어있으면 실패")
    public void createPost_fail_Images_filedEmpty() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(
                        PostImageRequest.builder().fileKey("img1.jpg").build(),
                        PostImageRequest.builder().imageOrder(2).build()
                ),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Post 작성 요청 - title이 blank이면 실패")
    public void write_fail_titleBlank() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                " ",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - oneLineDescription이 blank이면 실패")
    public void write_fail_oneLineDescriptionBlank() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                " ",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Post 작성 요청 - postMoodTypes 비어있으면 실패")
    public void createPost_fail_MoodTypesEmpty() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - spaceType이 null이면 실패")
    public void write_fail_spaceTypeNull() throws Exception {

        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                null,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - spaceAddress가 blank면 실패")
    public void write_fail_spaceAddressBlank() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                " ",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .packageThemeType("SNAP")
                        .snapSubTheme("FAMILY")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - packages가 null이면 실패")
    public void write_fail_packagesIsNull() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                null
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - packages가 비어있으면(리스트 크기가 1이면) 실패")
    public void write_fail_packagesIsEmpty() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of()
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - packages의 특정 필드가 비어있으면 실패1")
    public void write_fail_packagesFiledIsNull1() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageNo("pkg-001")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - packages의 특정 필드가 비어있으면 실패2")
    public void write_fail_packagesFiledIsNull2() throws Exception {
        CreatePostRequest webReq = createCreatePostWebReq(
                List.of(PostImageRequest.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                List.of(CreatePostRequest.PackageRequest.builder()
                        .packageThemeType("SNAP")
                        .build())
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private CreatePostRequest createCreatePostWebReq(
            List<PostImageRequest> postImages,
            String title,
            String oneLineDescription,
            String detailedDescription,
            List<PostMoodType> postMoodTypes,
            SpaceType spaceType,
            String spaceAddress,
            List<CreatePostRequest.PackageRequest> packages
    ) {
        return CreatePostRequest.builder()
                .postImages(postImages)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .packages(packages)
                .build();
    }

}