package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.result.LoadPostDetailResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.picus.core.post.domain.vo.PostMoodType.COZY;
import static com.picus.core.post.domain.vo.PostThemeType.BEAUTY;
import static com.picus.core.post.domain.vo.PostThemeType.SNAP;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LoadPostDetailServiceTest {
    @Mock private PostReadPort postReadPort;
    @Mock private UserReadPort userReadPort;

    @InjectMocks private LoadPostDetailService loadPostDetailService;

    @Test
    @DisplayName("Post 상세정보를 조회한다.")
    public void load_success() throws Exception {
        // given
        String postNo = "post-123";
        String authorNo = "expert-123";
        Post post = createPost(postNo, List.of("package-123"), authorNo,
                "테스트 제목", "한 줄 설명", "자세한 설명입니다.",
                List.of(BEAUTY, SNAP), List.of(SnapSubTheme.FAMILY),
                List.of(COZY), SpaceType.INDOOR, "서울특별시 강남구", false,
                List.of(
                        PostImage.builder()
                                .postImageNo("img-1")
                                .fileKey("img_1.jpg")
                                .imageOrder(1)
                                .build(),
                        PostImage.builder()
                                .postImageNo("img-2")
                                .fileKey("img_2.jpg")
                                .imageOrder(2)
                                .build()
                ));
        String nickname = "n";
        User user = User.builder()
                .nickname(nickname)
                .build();
        // then - stubbing
        stub(post, user);

        // when
        LoadPostDetailResult result = loadPostDetailService.load(postNo);

        // then
        assertThat(result.postNo()).isEqualTo(post.getPostNo());
        assertThat(result.authorInfo().expertNo()).isEqualTo(authorNo);
        assertThat(result.authorInfo().nickname()).isEqualTo(nickname);
        assertThat(result.title()).isEqualTo(post.getTitle());
        assertThat(result.oneLineDescription()).isEqualTo(post.getOneLineDescription());
        assertThat(result.detailedDescription()).isEqualTo(post.getDetailedDescription());
        assertThat(result.themeTypes()).isEqualTo(post.getPostThemeTypes());
        assertThat(result.snapSubThemes()).isEqualTo(post.getSnapSubThemes());
        assertThat(result.moodTypes()).isEqualTo(post.getPostMoodTypes());
        assertThat(result.spaceType()).isEqualTo(post.getSpaceType());
        assertThat(result.spaceAddress()).isEqualTo(post.getSpaceAddress());
        assertThat(result.packageNos()).isEqualTo(post.getPackageNos());
        assertThat(result.updatedAt()).isEqualTo(post.getUpdatedAt());
        assertThat(result.images()).hasSize(post.getPostImages().size())
                .extracting(
                        LoadPostDetailResult.PostImageResult::imageNo,
                        LoadPostDetailResult.PostImageResult::fileKey,
                        LoadPostDetailResult.PostImageResult::imageUrl, // TODO: filekey->url 변환 로직 필요
                        LoadPostDetailResult.PostImageResult::imageOrder
                ).containsExactlyInAnyOrder(
                        tuple("img-1", "img_1.jpg", "", 1),
                        tuple("img-2", "img_2.jpg", "", 2)
                );

        then(postReadPort).should().findById(postNo);
        then(userReadPort).should().findByExpertNo(authorNo);
    }

    private void stub(Post post, User user) {
        given(postReadPort.findById(post.getPostNo())).willReturn(Optional.of(post));
        given(userReadPort.findByExpertNo(post.getAuthorNo()))
                .willReturn(user);
    }

    private Post createPost(String postNo, List<String> packageNos, String authorNo, String title, String oneLineDescription,
                            String detailedDescription, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                            List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                            boolean isPinned, List<PostImage> postImages) {
        return Post.builder()
                .postNo(postNo)
                .packageNos(packageNos)
                .authorNo(authorNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .snapSubThemes(snapSubThemes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .isPinned(isPinned)
                .postImages(postImages)
                .build();
    }
}