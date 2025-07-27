package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertQueryPort;
import com.picus.core.expert.application.port.out.ExpertCommandPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.post.application.port.in.mapper.WritePostAppMapper;
import com.picus.core.post.application.port.in.request.ChangeStatus;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq.UpdatePostImageAppReq;
import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.post.application.port.out.PostCommandPort;
import com.picus.core.post.application.port.out.PostQueryPort;
import com.picus.core.post.domain.model.Post;
import com.picus.core.post.domain.model.PostImage;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
class PostInfoCommandServiceTest {

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
    @Mock
    WritePostAppMapper writePostAppMapper;

    @InjectMocks
    PostInfoCommandService postInfoCommandService;

    @Test
    @DisplayName("게시물을 작성한다.")
    public void write_success() throws Exception {
        // given
        WritePostAppReq req = WritePostAppReq.builder()
                .currentUserNo("user-456")
                .build();

        User user = Mockito.mock(User.class);
        given(userQueryPort.findById(req.currentUserNo())).willReturn(user);
        String expertNo = "expert_no";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = Mockito.mock(Post.class);
        given(writePostAppMapper.toDomain(req, expertNo)).willReturn(post);

        // when
        postInfoCommandService.write(req);

        // then
        InOrder inOrder = Mockito.inOrder(
                userQueryPort, user, writePostAppMapper, postCommandPort
        );
        then(userQueryPort).should(inOrder).findById(req.currentUserNo());
        then(user).should(inOrder).getExpertNo();
        then(writePostAppMapper).should(inOrder).toDomain(req, expertNo);
        then(postCommandPort).should(inOrder).save(post);
    }

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
        postInfoCommandService.update(updatePostAppReq);

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
        then(expertCommandPort).should().updateExpert(mockExpert);
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

        assertThatThrownBy(() -> postInfoCommandService.update(req))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("게시물을 삭제한다.")
    public void delete_success() throws Exception {
        // given
        String postNo = "post-123";
        String currentUserNo = "user-123";

        // stubbing
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        String expertNo = "expert-123";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = mock(Post.class);
        given(postQueryPort.findById(postNo)).willReturn(Optional.of(post));
        given(post.getAuthorNo()).willReturn(expertNo);
        Expert expert = mock(Expert.class);
        given(expertQueryPort.findById(expertNo)).willReturn(Optional.of(expert));
        LocalDateTime lastPostAt = LocalDateTime.MAX;
        given(postQueryPort.findTopUpdatedAtByExpertNo(post.getAuthorNo())).willReturn(Optional.of(lastPostAt));


        // when
        postInfoCommandService.delete(postNo, currentUserNo);

        // then
        then(userQueryPort).should().findById(currentUserNo);
        then(user).should().getExpertNo();
        then(postQueryPort).should().findById(postNo);
        then(postCommandPort).should().delete(postNo);
        then(expertQueryPort).should().findById(expertNo);
        then(expert).should().decreaseActivityCount();
        then(postQueryPort).should().findTopUpdatedAtByExpertNo(post.getAuthorNo());
        then(expert).should().updateLastActivityAt(lastPostAt);
        then(expertCommandPort).should().updateExpert(expert);
    }

    @Test
    @DisplayName("게시물을 삭제할 때 현재 사용자의 expertNo와 Post의 authorNo가 일치하지 않으면 에러 발생")
    public void delete_if_expertNo_NotEqual() throws Exception {
        // given
        String postNo = "post-123";
        String currentUserNo = "user-123";

        // stubbing
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        String expertNo = "expert-123";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = mock(Post.class);
        given(postQueryPort.findById(postNo)).willReturn(Optional.of(post));
        given(post.getAuthorNo()).willReturn("expert-345");

        // when // then
        assertThatThrownBy(() -> postInfoCommandService.delete(postNo, currentUserNo))
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