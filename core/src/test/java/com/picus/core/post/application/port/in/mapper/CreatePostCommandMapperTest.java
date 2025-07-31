package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class CreatePostCommandMapperTest {

    private final CreatePostCommandMapper mapper = new CreatePostCommandMapper();

    @Test
    @DisplayName("CreatePostCommand -> Post 도메인 모델로 매핑")
    void toDomain_success() {
        // given
        CreatePostCommand req = CreatePostCommand.builder()
                .postImages(List.of(
                        PostImage.builder()
                                .fileKey("img_1.jpg")
                                .imageOrder(1)
                                .build(),
                        PostImage.builder()
                                .fileKey("img_2.jpg")
                                .imageOrder(2)
                                .build()
                ))
                .title("테스트 제목")
                .oneLineDescription("한 줄 설명")
                .detailedDescription("자세한 설명입니다.")
                .postThemeTypes(List.of(PostThemeType.BEAUTY, PostThemeType.SNAP))
                .snapSubThemes(List.of(SnapSubTheme.FAMILY))
                .postMoodTypes(List.of(PostMoodType.COZY))
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울특별시 강남구")
                .packageNo("package-123")
                .currentUserNo("user-456")
                .build();

        String expertNo = "expert-789";

        // when
        Post post = mapper.toDomain(req, expertNo);

        // then
        assertThat(post.getAuthorNo()).isEqualTo("expert-789");
        assertThat(post.getPackageNo()).isEqualTo("package-123");
        assertThat(post.getTitle()).isEqualTo("테스트 제목");
        assertThat(post.getOneLineDescription()).isEqualTo("한 줄 설명");
        assertThat(post.getDetailedDescription()).isEqualTo("자세한 설명입니다.");
        assertThat(post.getPostThemeTypes()).containsExactly(PostThemeType.BEAUTY, PostThemeType.SNAP);
        assertThat(post.getSnapSubThemes()).containsExactly(SnapSubTheme.FAMILY);
        assertThat(post.getPostMoodTypes()).containsExactly(PostMoodType.COZY);
        assertThat(post.getSpaceType()).isEqualTo(SpaceType.INDOOR);
        assertThat(post.getSpaceAddress()).isEqualTo("서울특별시 강남구");
        assertThat(post.getPostImages()).hasSize(2)
                .extracting(PostImage::getFileKey, PostImage::getImageOrder)
                .containsExactlyInAnyOrder(
                        tuple("img_1.jpg", 1),
                        tuple("img_2.jpg", 2)
                );
        assertThat(post.getIsPinned()).isFalse();
    }
}