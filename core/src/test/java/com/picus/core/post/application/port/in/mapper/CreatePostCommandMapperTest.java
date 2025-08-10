package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
                .postMoodTypes(List.of(PostMoodType.COZY))
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울특별시 강남구")
                .packages(List.of(
                        CreatePostCommand.PackageCommand.builder()
                                .packageNo("package-123")
                                .packageThemeType("SNAP")
                                .snapSubTheme("FAMILY")
                                .build()
                ))
                .authorNo("user-456")
                .build();

        String expertNo = "expert-789";

        // when
        Post post = mapper.toDomain(req, expertNo);

        // then
        assertThat(post.getAuthorNo()).isEqualTo("expert-789");
        assertThat(post.getPackageNos()).containsExactly("package-123");
        assertThat(post.getTitle()).isEqualTo("테스트 제목");
        assertThat(post.getOneLineDescription()).isEqualTo("한 줄 설명");
        assertThat(post.getDetailedDescription()).isEqualTo("자세한 설명입니다.");
        assertThat(post.getPostThemeTypes()).containsExactlyInAnyOrder(PostThemeType.SNAP);
        assertThat(post.getSnapSubThemes()).containsExactlyInAnyOrder(SnapSubTheme.FAMILY);
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

    @Test
    @DisplayName("중복된 packageThemeType, snapSubTheme이 있어도 하나만 들어간다.")
    void toDomain_success_duplicateTheme() {
        // given
        CreatePostCommand req = CreatePostCommand.builder()
                .packages(List.of(
                        CreatePostCommand.PackageCommand.builder()
                                .packageNo("package-123")
                                .packageThemeType("SNAP")
                                .snapSubTheme("FAMILY")
                                .build(),
                        CreatePostCommand.PackageCommand.builder()
                                .packageNo("package-234")
                                .packageThemeType("SNAP")
                                .snapSubTheme("FAMILY")
                                .build()
                ))
                .build();

        String expertNo = "expert-789";

        // when
        Post post = mapper.toDomain(req, expertNo);

        // then
        assertThat(post.getPostThemeTypes()).hasSize(1)
                        .containsExactlyInAnyOrder(PostThemeType.SNAP);
        assertThat(post.getSnapSubThemes()).hasSize(1)
                        .containsExactlyInAnyOrder(SnapSubTheme.FAMILY);
    }

    @Test
    @DisplayName("잘못된 packageThemeType가 들어가면 예외가 발생한다.")
    void toDomain_fail_wrong_packageThemeType() {
        // given
        CreatePostCommand req = CreatePostCommand.builder()
                .packages(List.of(
                        CreatePostCommand.PackageCommand.builder()
                                .packageNo("package-123")
                                .packageThemeType("SNAP_")
                                .snapSubTheme("FAMILY")
                                .build()
                ))
                .build();

        String expertNo = "expert-789";

        // when // then
        assertThatThrownBy(() -> mapper.toDomain(req, expertNo))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("잘못된 snapSubTheme가 들어가면 예외가 발생한다.")
    void toDomain_fail_wrong_snapSubTheme() {
        // given
        CreatePostCommand req = CreatePostCommand.builder()
                .packages(List.of(
                        CreatePostCommand.PackageCommand.builder()
                                .packageNo("package-123")
                                .packageThemeType("SNAP")
                                .snapSubTheme("FAMILY_")
                                .build()
                ))
                .build();

        String expertNo = "expert-789";

        // when // then
        assertThatThrownBy(() -> mapper.toDomain(req, expertNo))
                .isInstanceOf(RestApiException.class);
    }
}