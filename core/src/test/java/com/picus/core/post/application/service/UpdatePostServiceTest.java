package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertQueryPort;
import com.picus.core.expert.application.port.out.ExpertCommandPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.request.ChangeStatus;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq.UpdatePostImageAppReq;
import com.picus.core.post.application.port.out.PostCommandPort;
import com.picus.core.post.application.port.out.PostQueryPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class UpdatePostServiceTest {

    @Mock
    UserQueryPort userQueryPort;
    @Mock
    ExpertQueryPort expertQueryPort;
    @Mock
    ExpertCommandPort expertCommandPort;
    @Mock
    PostCommandPort postCommandPort;
    @Mock
    PostQueryPort postQueryPort;

    @InjectMocks
    UpdatePostService updatePostService;


    @Test
    @DisplayName("게시물을 수정한다. Post, PostImage가 수정된다.")
    public void update_success() throws Exception {
        // given
        String postNo = "post-123";
        String userNo = "mockUser-123";
        // PostImage
        UpdatePostImageAppReq newImage =
                createUpdatePostImageAppReq(null, "new.jpg", 1, ChangeStatus.NEW);
        UpdatePostImageAppReq updateImage =
                createUpdatePostImageAppReq("img-123", "upt.jpg", 2, ChangeStatus.UPDATE);
        UpdatePostImageAppReq deleteImage =
                createUpdatePostImageAppReq("img-456", null, null, ChangeStatus.DELETE);

        UpdatePostAppReq updatePostAppReq =
                createUpdatePostAppReq(
                        postNo, newImage, updateImage, deleteImage,
                        "title", "one", "detail",
                        List.of(PostThemeType.BEAUTY), List.of(PostMoodType.VINTAGE),
                        SpaceType.INDOOR, "space", "pkg-123", userNo);

        User mockUser = mock(User.class);
        given(userQueryPort.findById(userNo)).willReturn(mockUser);
        String expertNo = "expert-123";
        given(mockUser.getExpertNo()).willReturn(expertNo);
        Post post = Post.builder().authorNo(expertNo).build();
        Post spyPost = spy(post);
        given(postQueryPort.findById(postNo)).willReturn(Optional.of(spyPost));
        Expert mockExpert = mock(Expert.class);
        given(expertQueryPort.findById(expertNo)).willReturn(Optional.of(mockExpert));

        // when
        updatePostService.update(updatePostAppReq);

        // then
        then(userQueryPort).should().findById(userNo);
        then(mockUser).should().getExpertNo();
        then(postQueryPort).should().findById(postNo);
        then(spyPost).should().updatePost("title", "one", "detail",
                List.of(PostThemeType.BEAUTY), List.of(PostMoodType.VINTAGE),
                SpaceType.INDOOR, "space", "pkg-123");
        then(spyPost).should().addPostImage(PostImage.builder()
                .fileKey(newImage.fileKey())
                .imageOrder(newImage.imageOrder())
                .build());
        then(spyPost).should().updatePostImage(PostImage.builder()
                .postImageNo(updateImage.postImageNo())
                .fileKey(updateImage.fileKey())
                .imageOrder(updateImage.imageOrder())
                .build());
        then(spyPost).should().deletePostImage(deleteImage.postImageNo());
        then(postCommandPort).should().update(spyPost, List.of(deleteImage.postImageNo()));
        then(expertQueryPort).should().findById(expertNo);
        then(mockExpert).should().updateLastActivityAt(any(LocalDateTime.class));
        then(expertCommandPort).should().update(mockExpert);
    }

    @DisplayName("Post를 수정할 때, 이미지 순서 중복 시 예외 발생")
    @Test
    void update_fail_dueToImageOrderDuplication() {
        // given
        UpdatePostImageAppReq image1 = createUpdatePostImageAppReq(null, "a.jpg", 1, ChangeStatus.NEW);
        UpdatePostImageAppReq image2 = createUpdatePostImageAppReq(null, "b.jpg", 1, ChangeStatus.NEW); // imageOrder 중복
        UpdatePostAppReq req = UpdatePostAppReq.builder()
                .postImages(List.of(image1, image2))
                .build();

        // when & then

        assertThatThrownBy(() -> updatePostService.update(req))
                .isInstanceOf(RestApiException.class);
    }

    /**
     * private 메서드
     */
    private UpdatePostAppReq createUpdatePostAppReq(
            String postNo, UpdatePostImageAppReq newImage, UpdatePostImageAppReq updateImage,
            UpdatePostImageAppReq deleteImage, String title, String oneLine, String detail,
            List<PostThemeType> postThemeTypes, List<PostMoodType> postMoodTypes,
            SpaceType spaceType, String spaceAddress, String packageNo, String userNo) {
        return UpdatePostAppReq.builder()
                .postNo(postNo)
                .postImages(List.of(newImage, updateImage, deleteImage))
                .title(title)
                .oneLineDescription(oneLine)
                .detailedDescription(detail)
                .postThemeTypes(postThemeTypes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .packageNo(packageNo)
                .currentUserNo(userNo)
                .build();
    }

    private UpdatePostImageAppReq createUpdatePostImageAppReq(
            String postImageNo, String fileKey, Integer imageOrder, ChangeStatus changeStatus) {
        return UpdatePostImageAppReq.builder()
                .postImageNo(postImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .changeStatus(changeStatus)
                .build();
    }

}