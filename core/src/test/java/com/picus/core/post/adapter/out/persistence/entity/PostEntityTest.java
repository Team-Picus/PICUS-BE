package com.picus.core.post.adapter.out.persistence.entity;

import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostEntityTest {

    @Test
    @DisplayName("PostEntity를 수정한다.")
    public void updatePostEntity() throws Exception {
        // given
        PostEntity postEntity = createPostEntity(
                "package-123", "expert-456", "제목", "설명",
                "상세 설명", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "서울시 강남구", false
        );

        // when
        postEntity.updatePostEntity(
                "package-999", "새 제목", "새 설명",
                "새 상세 설명",
                List.of(PostThemeType.EVENT), List.of(PostMoodType.INTENSE),
                SpaceType.OUTDOOR, "부산시 해운대구", true
        );

        // then
        // then
        assertThat(postEntity.getTitle()).isEqualTo("새 제목");
        assertThat(postEntity.getOneLineDescription()).isEqualTo("새 설명");
        assertThat(postEntity.getDetailedDescription()).isEqualTo("새 상세 설명");
        assertThat(postEntity.getPostThemeTypes()).isEqualTo(List.of(PostThemeType.EVENT));
        assertThat(postEntity.getPostMoodTypes()).isEqualTo(List.of(PostMoodType.INTENSE));
        assertThat(postEntity.getSpaceType()).isEqualTo(SpaceType.OUTDOOR);
        assertThat(postEntity.getSpaceAddress()).isEqualTo("부산시 해운대구");
        assertThat(postEntity.getPackageNo()).isEqualTo("package-999");
        assertThat(postEntity.getIsPinned()).isTrue();
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
}