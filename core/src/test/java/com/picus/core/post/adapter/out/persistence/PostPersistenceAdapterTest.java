package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.adapter.out.persistence.mapper.PostImagePersistenceMapper;
import com.picus.core.post.adapter.out.persistence.mapper.PostPersistenceMapper;
import com.picus.core.post.adapter.out.persistence.repository.PostImageJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.model.Post;
import com.picus.core.post.domain.model.PostImage;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@Import({
        PostPersistenceAdapter.class,
        PostPersistenceMapper.class,
        PostImagePersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostPersistenceAdapterTest {

    @Autowired
    PostJpaRepository postJpaRepository;
    @Autowired
    PostImageJpaRepository postImageJpaRepository;

    @Autowired
    private PostPersistenceAdapter postPersistenceAdapter;

    @Test
    @DisplayName("Post를 데이터베이스에 저장한다.")
    public void save_success() throws Exception {
        // given
        Post post = createPost(
                "package-123", "expert-456",
                "테스트 제목", "한 줄 설명",
                "자세한 설명입니다.", List.of(PostThemeType.BEAUTY, PostThemeType.EVENT),
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
        Post saved = postPersistenceAdapter.save(post);

        // then
        // PostEntity
        Optional<PostEntity> optionalPostEntity = postJpaRepository.findById(saved.getPostNo());
        assertThat(optionalPostEntity).isPresent();
        PostEntity postEntity = optionalPostEntity.get();
        assertThat(postEntity.getPostNo()).isEqualTo(saved.getPostNo());
        assertThat(postEntity.getPackageNo()).isEqualTo("package-123");
        assertThat(postEntity.getExpertNo()).isEqualTo("expert-456");
        assertThat(postEntity.getTitle()).isEqualTo("테스트 제목");
        assertThat(postEntity.getOneLineDescription()).isEqualTo("한 줄 설명");
        assertThat(postEntity.getDetailedDescription()).isEqualTo("자세한 설명입니다.");
        assertThat(postEntity.getPostThemeTypes()).isEqualTo(List.of(PostThemeType.BEAUTY, PostThemeType.EVENT));
        assertThat(postEntity.getPostMoodTypes()).isEqualTo(List.of(PostMoodType.COZY));
        assertThat(postEntity.getSpaceType()).isEqualTo(SpaceType.INDOOR);
        assertThat(postEntity.getSpaceAddress()).isEqualTo("서울특별시 강남구");
        assertThat(postEntity.getIsPinned()).isEqualTo(false);

        // PostImageEntity
        List<PostImageEntity> imageEntities = postImageJpaRepository.findByPostEntity_PostNo(saved.getPostNo());
        assertThat(imageEntities).hasSize(2)
            .extracting(PostImageEntity::getFileKey, PostImageEntity::getImageOrder)
            .containsExactlyInAnyOrder(
                tuple("img_1.jpg", 1),
                tuple("img_2.jpg", 2)
            );

    }

    private Post createPost(String packageNo, String authorNo, String title, String oneLineDescription,
                            String detailedDescription, List<PostThemeType> postThemeTypes,
                            List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                            boolean isPinned, List<PostImage> postImages) {
        return Post.builder()
                .packageNo(packageNo)
                .authorNo(authorNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .isPinned(isPinned)
                .postImages(postImages)
                .build();
    }

}