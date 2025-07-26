package com.picus.core.post.adapter.out.persistence.repository;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostImageJpaRepositoryTest {

    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private PostImageJpaRepository postImageJpaRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("특정 Post의 모든 PostEntity의 imageOrder를 음수값으로 바꾼다")
    public void shiftAllImageOrdersToNegative_success() throws Exception {
        // given
        PostEntity postEntity = createPostEntity(
                "package-123", "expert-456", "제목", "설명",
                "상세 설명", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "서울시 강남구", false
        );
        postJpaRepository.save(postEntity);

        PostImageEntity postImageEntity1 = createPostImageEntity("f1_jpg", 1, postEntity);
        PostImageEntity postImageEntity2 = createPostImageEntity("f2_jpg", 2, postEntity);
        postImageJpaRepository.saveAll(List.of(postImageEntity1, postImageEntity2));
        clearPersistenceContext();

        // when
        postImageJpaRepository.shiftAllImageOrdersToNegative(postEntity.getPostNo());
        clearPersistenceContext(); // 쿼리 반영 및 영속성 컨텍스트 초기화

        // then
        List<PostImageEntity> results = postImageJpaRepository.findAllByPostEntity_PostNoOrderByImageOrder(postEntity.getPostNo());
        List<Integer> imageOrderList = results.stream()
                .map(PostImageEntity::getImageOrder)
                .toList();

        assertThat(imageOrderList).containsExactlyInAnyOrder(-1, -2);
    }

    private PostEntity createPostEntity(String packageNo, String expertNo, String title, String oneLineDescription,
                                        String detailedDescription, List<PostThemeType> postThemeTypes,
                                        List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                                        boolean isPinned) {
        return PostEntity.builder()
                .packageNo(packageNo)
                .expertNo(expertNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .isPinned(isPinned)
                .build();
    }
    private PostImageEntity createPostImageEntity(String fileKey, int imageOrder, PostEntity postEntity) {
        return PostImageEntity.builder()
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .postEntity(postEntity)
                .build();
    }
    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }


}