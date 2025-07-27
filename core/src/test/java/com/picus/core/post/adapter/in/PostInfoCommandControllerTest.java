package com.picus.core.post.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostWebReq;
import com.picus.core.post.adapter.in.web.data.request.WritePostWebReq;
import com.picus.core.post.adapter.in.web.data.request.WritePostWebReq.PostImageWebReq;
import com.picus.core.post.adapter.in.web.mapper.UpdatePostWebMapper;
import com.picus.core.post.adapter.in.web.mapper.WritePostWebMapper;
import com.picus.core.post.application.port.in.PostInfoCommand;
import com.picus.core.post.application.port.in.request.ChangeStatus;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq;
import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
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

import static com.picus.core.post.application.port.in.request.ChangeStatus.NEW;
import static com.picus.core.post.application.port.in.request.ChangeStatus.UPDATE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostInfoCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostInfoCommandControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostInfoCommand postInfoCommand;
    @MockitoBean
    private WritePostWebMapper writePostWebMapper;
    @MockitoBean
    private UpdatePostWebMapper updatePostWebMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Post 작성 요청 - 성공")
    public void write_success() throws Exception {
        // given
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(
                        PostImageWebReq.builder().fileKey("img1.jpg").imageOrder(1).build(),
                        PostImageWebReq.builder().fileKey("img2.jpg").imageOrder(2).build()
                ),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
        );

        String currenUserNo = TEST_USER_ID;

        WritePostAppReq writePostAppReq = Mockito.mock(WritePostAppReq.class);
        given(writePostWebMapper.toAppReq(webReq, currenUserNo)).willReturn(writePostAppReq);

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
        then(writePostWebMapper).should().toAppReq(webReq, currenUserNo);
        then(postInfoCommand).should().write(writePostAppReq);
    }

    @Test
    @DisplayName("Post 작성 요청 - postImages 비어있으면 실패")
    public void writePost_fail_ImagesEmpty() throws Exception {
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - postImages 안에 필드가 비어있으면 실패")
    public void writePost_fail_Images_filedEmpty() throws Exception {
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(
                        PostImageWebReq.builder().fileKey("img1.jpg").build(),
                        PostImageWebReq.builder().imageOrder(2).build()
                ),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
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
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(PostImageWebReq.builder().fileKey("img.jpg").imageOrder(1).build()),
                " ",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
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
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(PostImageWebReq.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                " ",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - detailedDescription이 blank이면 실패")
    public void write_fail_detailedDescriptionBlank() throws Exception {
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(PostImageWebReq.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                " ",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - postThemeTypes 비어있으면 실패")
    public void writePost_fail_ThemeTypesEmpty() throws Exception {
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(PostImageWebReq.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - postMoodTypes 비어있으면 실패")
    public void writePost_fail_MoodTypesEmpty() throws Exception {
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(PostImageWebReq.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
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

        WritePostWebReq webReq = createWritePostWebReq(
                List.of(PostImageWebReq.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                null,
                "서울시 강남구",
                "pkg-001"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 작성 요청 - spaceAddress가 blank이면 실패")
    public void write_fail_spaceAddressBlank() throws Exception {
        WritePostWebReq webReq = createWritePostWebReq(
                List.of(PostImageWebReq.builder().fileKey("img.jpg").imageOrder(1).build()),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                " ",
                "pkg-001"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post 수정 요청 - 성공")
    public void updatePost_success() throws Exception {
        // given
        UpdatePostWebReq webReq = createUpdatePostWebReq(List.of(
                        UpdatePostWebReq.PostImageWebReq.builder().fileKey("img1.jpg").imageOrder(1).changeStatus(NEW).build(),
                        createPostImageWebReq(null, "img1.jpg", 1, NEW),
                        createPostImageWebReq("img-123", "img2.jpg", 2, UPDATE)
                ), "테스트 제목", "한 줄 설명", "자세한 설명입니다.",
                List.of(PostThemeType.BEAUTY, PostThemeType.EVENT),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울시 강남구", "pkg-001");

        String postNo = "post-123";
        String currenUserNo = TEST_USER_ID;

        UpdatePostAppReq updatePostAppReq = mock(UpdatePostAppReq.class);
        given(updatePostWebMapper.toAppReq(webReq, postNo, currenUserNo))
                .willReturn(updatePostAppReq);

        // when
        mockMvc.perform(patch("/api/v1/posts/{post_no}", postNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        // then
        then(updatePostWebMapper).should().toAppReq(webReq, postNo, currenUserNo);
        then(postInfoCommand).should().update(updatePostAppReq);
    }

    private UpdatePostWebReq.PostImageWebReq createPostImageWebReq(String postImageNo, String fileKey, int imageOrder, ChangeStatus changeStatus) {
        return UpdatePostWebReq.PostImageWebReq.builder()
                .postImageNo(postImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .changeStatus(changeStatus)
                .build();
    }

    private WritePostWebReq createWritePostWebReq(
            List<PostImageWebReq> postImages,
            String title,
            String oneLineDescription,
            String detailedDescription,
            List<PostThemeType> postThemeTypes,
            List<PostMoodType> postMoodTypes,
            SpaceType spaceType,
            String spaceAddress,
            String packageNo
    ) {
        return WritePostWebReq.builder()
                .postImages(postImages)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .packageNo(packageNo)
                .build();
    }

    private UpdatePostWebReq createUpdatePostWebReq(List<UpdatePostWebReq.PostImageWebReq> postImages, String title, String oneLineDescription, String detailedDescription, List<PostThemeType> postThemeTypes, List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress, String packageNo) {
        return UpdatePostWebReq.builder()
                .postImages(postImages)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .packageNo(packageNo)
                .build();
    }
}