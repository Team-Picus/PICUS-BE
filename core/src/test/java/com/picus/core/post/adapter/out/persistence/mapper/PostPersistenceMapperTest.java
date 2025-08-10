package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PostPersistenceMapperTest {

    final PostPersistenceMapper mapper = new PostPersistenceMapper();
    @Test
    @DisplayName("Post -> PostEntity 매핑")
    public void toEntity() throws Exception {
        // given
        Post post = createPost(
                List.of("package-123"), "expert-456",
                "테스트 제목", "한 줄 설명", "자세한 설명입니다.",
                List.of(PostThemeType.BEAUTY, PostThemeType.SNAP), List.of(SnapSubTheme.ADMISSION),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울특별시 강남구", false,
                List.of(
                        PostImage.builder()
                                .fileKey("img_1.jpg")
                                .imageOrder(1)
                                .build(),
                        PostImage.builder()
                                .fileKey("img_2.jpg")
                                .imageOrder(2)
                                .build()
                ));

        // when
        PostEntity entity = mapper.toEntity(post);

        // then
        assertThat(entity.getTitle()).isEqualTo(post.getTitle());
        assertThat(entity.getPackageNos()).isEqualTo(post.getPackageNos());
        assertThat(entity.getExpertNo()).isEqualTo(post.getAuthorNo());
        assertThat(entity.getOneLineDescription()).isEqualTo(post.getOneLineDescription());
        assertThat(entity.getDetailedDescription()).isEqualTo(post.getDetailedDescription());
        assertThat(entity.getPostThemeTypes()).isEqualTo(post.getPostThemeTypes());
        assertThat(entity.getSnapSubThemes()).isEqualTo(post.getSnapSubThemes());
        assertThat(entity.getPostMoodTypes()).isEqualTo(post.getPostMoodTypes());
        assertThat(entity.getSpaceType()).isEqualTo(post.getSpaceType());
        assertThat(entity.getSpaceAddress()).isEqualTo(post.getSpaceAddress());
        assertThat(entity.getIsPinned()).isEqualTo(post.getIsPinned());
    }

    @Test
    @DisplayName("PostEntity -> Post 매핑")
    public void toDomain() throws Exception {
        // given
        PostEntity postEntity = createPostEntity(
                "post-123", List.of("package-123"), "expert-456",
                "테스트 제목", "한 줄 설명", "자세한 설명입니다.",
                List.of(PostThemeType.BEAUTY, PostThemeType.SNAP), List.of(SnapSubTheme.FAMILY),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울특별시 강남구", false,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                LocalDateTime.of(2025, 1, 1, 1, 2),
                LocalDateTime.of(2025, 1, 1, 1, 3)
        );

        PostImage postImage = PostImage.builder()
                .fileKey("file_key")
                .imageOrder(1)
                .build();

        // when
        Post post = mapper.toDomain(postEntity, List.of(postImage));

        // then
        assertThat(post.getPostNo()).isEqualTo(postEntity.getPostNo());
        assertThat(post.getPackageNos()).isEqualTo(postEntity.getPackageNos());
        assertThat(post.getAuthorNo()).isEqualTo(postEntity.getExpertNo());
        assertThat(post.getTitle()).isEqualTo(postEntity.getTitle());
        assertThat(post.getOneLineDescription()).isEqualTo(postEntity.getOneLineDescription());
        assertThat(post.getDetailedDescription()).isEqualTo(postEntity.getDetailedDescription());
        assertThat(post.getPostThemeTypes()).isEqualTo(postEntity.getPostThemeTypes());
        assertThat(post.getSnapSubThemes()).isEqualTo(postEntity.getSnapSubThemes());
        assertThat(post.getPostMoodTypes()).isEqualTo(postEntity.getPostMoodTypes());
        assertThat(post.getSpaceType()).isEqualTo(postEntity.getSpaceType());
        assertThat(post.getSpaceAddress()).isEqualTo(postEntity.getSpaceAddress());
        assertThat(post.getIsPinned()).isEqualTo(postEntity.getIsPinned());
        assertThat(post.getCreatedAt()).isEqualTo(postEntity.getCreatedAt());
        assertThat(post.getUpdatedAt()).isEqualTo(postEntity.getUpdatedAt());
        assertThat(post.getDeletedAt()).isEqualTo(postEntity.getDeletedAt());
        assertThat(post.getPostImages()).containsExactly(postImage);
    }

    private Post createPost(List<String> packageNos, String authorNo, String title, String oneLineDescription,
                            String detailedDescription, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                            List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                            boolean isPinned, List<PostImage> postImages) {
        return Post.builder()
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

    private PostEntity createPostEntity(String postNo, List<String> packageNos, String authorNo, String title, String oneLineDescription,
                                        String detailedDescription, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                                        List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                                        boolean isPinned, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return PostEntity.builder()
                .postNo(postNo)
                .packageNos(packageNos)
                .expertNo(authorNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .snapSubThemes(snapSubThemes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .isPinned(isPinned)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }
}