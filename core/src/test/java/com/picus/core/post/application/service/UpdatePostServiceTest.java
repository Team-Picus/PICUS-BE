package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.application.port.out.ExpertUpdatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.command.ChangeStatus;
import com.picus.core.post.application.port.in.command.UpdatePostCommand;
import com.picus.core.post.application.port.in.command.UpdatePostCommand.UpdatePostImageCommand;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.application.port.out.PostUpdatePort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
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
    ExpertReadPort expertReadPort;
    @Mock
    ExpertUpdatePort expertUpdatePort;
    @Mock
    PostUpdatePort postUpdatePort;
    @Mock
    PostReadPort postReadPort;

    @InjectMocks
    UpdatePostService updatePostService;


    @Test
    @DisplayName("게시물을 수정한다. Post, PostImage가 수정된다.")
    public void update_success() throws Exception {
        // given
        String postNo = "post-123";
        String userNo = "mockUser-123";
        // PostImage
        UpdatePostImageCommand newImage =
                createUpdatePostImageAppReq(null, "new.jpg", 1, ChangeStatus.NEW);
        UpdatePostImageCommand updateImage =
                createUpdatePostImageAppReq("img-123", "upt.jpg", 2, ChangeStatus.UPDATE);
        UpdatePostImageCommand deleteImage =
                createUpdatePostImageAppReq("img-456", null, null, ChangeStatus.DELETE);


        UpdatePostCommand cmd =
                createUpdatePostAppReq(
                        postNo, newImage, updateImage, deleteImage,
                        "title", "one", "detail", List.of(PostMoodType.VINTAGE),
                        SpaceType.INDOOR, "space",
                        List.of(UpdatePostCommand.PackageCommand.builder()
                                .packageNo("pkg-123")
                                .packageThemeType("SNAP")
                                .snapSubTheme("PROFILE")
                                .build()
                        ),
                        userNo);

        Post post = Post.builder().authorNo(userNo).build();
        Post spyPost = spy(post);
        given(postReadPort.findById(postNo)).willReturn(Optional.of(spyPost));
        Expert mockExpert = mock(Expert.class);
        given(expertReadPort.findById(userNo)).willReturn(Optional.of(mockExpert));

        // when
        updatePostService.update(cmd);

        // then
        then(postReadPort).should().findById(postNo);
        then(spyPost).should().updatePost("title", "one", "detail",
                List.of("pkg-123"), List.of(PostThemeType.SNAP), List.of(SnapSubTheme.PROFILE),
                List.of(PostMoodType.VINTAGE), SpaceType.INDOOR, "space");
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
        then(postUpdatePort).should().updateWithPostImage(spyPost, List.of(deleteImage.postImageNo()));
        then(expertReadPort).should().findById(cmd.currentUserNo());
        then(mockExpert).should().updateLastActivityAt(any(LocalDateTime.class));
        then(expertUpdatePort).should().update(mockExpert);
    }

    @DisplayName("Post를 수정할 때, 이미지 순서 중복 시 예외 발생")
    @Test
    void update_fail_dueToImageOrderDuplication() {
        // given
        UpdatePostImageCommand image1 = createUpdatePostImageAppReq(null, "a.jpg", 1, ChangeStatus.NEW);
        UpdatePostImageCommand image2 = createUpdatePostImageAppReq(null, "b.jpg", 1, ChangeStatus.NEW); // imageOrder 중복
        UpdatePostCommand req = UpdatePostCommand.builder()
                .postImages(List.of(image1, image2))
                .build();

        // when & then

        assertThatThrownBy(() -> updatePostService.update(req))
                .isInstanceOf(RestApiException.class);
    }

    /**
     * private 메서드
     */
    private UpdatePostCommand createUpdatePostAppReq(
            String postNo, UpdatePostImageCommand newImage, UpdatePostImageCommand updateImage,
            UpdatePostImageCommand deleteImage, String title, String oneLine, String detail,
            List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
            List<UpdatePostCommand.PackageCommand> packages, String userNo) {
        return UpdatePostCommand.builder()
                .postNo(postNo)
                .postImages(List.of(newImage, updateImage, deleteImage))
                .title(title)
                .oneLineDescription(oneLine)
                .detailedDescription(detail)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .packages(packages)
                .currentUserNo(userNo)
                .build();
    }

    private UpdatePostImageCommand createUpdatePostImageAppReq(
            String postImageNo, String fileKey, Integer imageOrder, ChangeStatus changeStatus) {
        return UpdatePostImageCommand.builder()
                .postImageNo(postImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .changeStatus(changeStatus)
                .build();
    }

}