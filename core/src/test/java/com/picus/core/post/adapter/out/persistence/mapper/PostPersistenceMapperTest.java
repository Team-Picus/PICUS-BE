package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.domain.model.Post;
import com.picus.core.post.domain.model.PostImage;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PostPersistenceMapperTest {

    final PostPersistenceMapper mapper = new PostPersistenceMapper();
    @Test
    @DisplayName("Post -> PostEntity 매핑")
    public void toEntity() throws Exception {
        // given
        Post post = Post.builder()
                .packageNo("package-123")
                .authorNo("expert-456")
                .title("테스트 제목")
                .oneLineDescription("한 줄 설명")
                .detailedDescription("자세한 설명입니다.")
                .postThemeTypes(List.of(PostThemeType.BEAUTY, PostThemeType.EVENT))
                .postMoodTypes(List.of(PostMoodType.COZY))
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울특별시 강남구")
                .isPinned(false)
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
                .build();

        // when
        PostEntity entity = mapper.toEntity(post);

        // then
        assertThat(entity.getTitle()).isEqualTo(post.getTitle());
        assertThat(entity.getPackageNo()).isEqualTo(post.getPackageNo());
        assertThat(entity.getExpertNo()).isEqualTo(post.getAuthorNo());
        assertThat(entity.getOneLineDescription()).isEqualTo(post.getOneLineDescription());
        assertThat(entity.getDetailedDescription()).isEqualTo(post.getDetailedDescription());
        assertThat(entity.getPostThemeTypes()).isEqualTo(post.getPostThemeTypes());
        assertThat(entity.getPostMoodTypes()).isEqualTo(post.getPostMoodTypes());
        assertThat(entity.getSpaceType()).isEqualTo(post.getSpaceType());
        assertThat(entity.getSpaceAddress()).isEqualTo(post.getSpaceAddress());
        assertThat(entity.getIsPinned()).isEqualTo(post.getIsPinned());
    }

}