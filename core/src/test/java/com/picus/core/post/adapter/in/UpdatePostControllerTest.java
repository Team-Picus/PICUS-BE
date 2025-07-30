package com.picus.core.post.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest;
import com.picus.core.post.adapter.in.web.mapper.UpdatePostWebMapper;
import com.picus.core.post.application.port.in.UpdatePostUseCase;
import com.picus.core.post.application.port.in.command.ChangeStatus;
import com.picus.core.post.application.port.in.command.UpdatePostCommand;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.picus.core.post.application.port.in.command.ChangeStatus.NEW;
import static com.picus.core.post.application.port.in.command.ChangeStatus.UPDATE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UpdatePostController.class)
@AutoConfigureMockMvc(addFilters = false)
class UpdatePostControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UpdatePostUseCase updatePostUseCase;
    @MockitoBean
    private UpdatePostWebMapper updatePostWebMapper;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("Post 수정 요청 - 성공")
    public void updatePost_success() throws Exception {
        // given
        UpdatePostRequest webReq = createUpdatePostWebReq(List.of(
                        UpdatePostRequest.PostImageRequest.builder().fileKey("img1.jpg").imageOrder(1).changeStatus(NEW).build(),
                        createPostImageWebReq(null, "img1.jpg", 1, NEW),
                        createPostImageWebReq("img-123", "img2.jpg", 2, UPDATE)
                ), "테스트 제목", "한 줄 설명", "자세한 설명입니다.",
                List.of(PostThemeType.BEAUTY, PostThemeType.EVENT),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울시 강남구", "pkg-001");

        String postNo = "post-123";
        String currenUserNo = TEST_USER_ID;

        UpdatePostCommand updatePostCommand = mock(UpdatePostCommand.class);
        given(updatePostWebMapper.toAppReq(webReq, postNo, currenUserNo))
                .willReturn(updatePostCommand);

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
        then(updatePostUseCase).should().update(updatePostCommand);
    }

    private UpdatePostRequest.PostImageRequest createPostImageWebReq(String postImageNo, String fileKey, int imageOrder, ChangeStatus changeStatus) {
        return UpdatePostRequest.PostImageRequest.builder()
                .postImageNo(postImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .changeStatus(changeStatus)
                .build();
    }

    private UpdatePostRequest createUpdatePostWebReq(List<UpdatePostRequest.PostImageRequest> postImages, String title, String oneLineDescription, String detailedDescription, List<PostThemeType> postThemeTypes, List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress, String packageNo) {
        return UpdatePostRequest.builder()
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