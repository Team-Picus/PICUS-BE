package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadPostDetailResponse;
import com.picus.core.post.application.port.in.result.LoadPostDetailResult;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class LoadPostDetailWebMapperTest {

    private LoadPostDetailWebMapper webMapper = new LoadPostDetailWebMapper();

    @Test
    @DisplayName("LoadPostDetailResult -> LoadPostDetailResponse")
    public void toResponse() {
        // given
        LoadPostDetailResult result = LoadPostDetailResult.builder()
                .postNo("post-001")
                .title("테스트 타이틀")
                .oneLineDescription("한줄 설명")
                .detailedDescription("상세 설명입니다.")
                .themeTypes(List.of(PostThemeType.SNAP))
                .snapSubThemes(List.of(SnapSubTheme.FAMILY))
                .moodTypes(List.of(PostMoodType.COZY))
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울시 강남구")
                .packageNos(List.of("pkg-1001"))
                .updatedAt(LocalDateTime.of(2023, 8, 2, 12, 0))
                .authorInfo(LoadPostDetailResult.AuthorInfo.builder()
                        .expertNo("exp-101")
                        .nickname("전문가1")
                        .build())
                .images(List.of(
                        LoadPostDetailResult.PostImageResult.builder()
                                .imageNo("img-1")
                                .fileKey("key-1")
                                .imageUrl("url-1")
                                .imageOrder(1)
                                .build()
                ))
                .build();

        // when
        LoadPostDetailResponse response = webMapper.toResponse(result);

        // then
        assertThat(response.postNo()).isEqualTo(result.postNo());
        assertThat(response.title()).isEqualTo(result.title());
        assertThat(response.oneLineDescription()).isEqualTo(result.oneLineDescription());
        assertThat(response.detailedDescription()).isEqualTo(result.detailedDescription());
        assertThat(response.spaceType()).isEqualTo(result.spaceType());
        assertThat(response.spaceAddress()).isEqualTo(result.spaceAddress());
        assertThat(response.packageNos()).isEqualTo(result.packageNos());
        assertThat(response.updatedAt()).isEqualTo(result.updatedAt());

        assertThat(response.authorInfo().expertNo()).isEqualTo(result.authorInfo().expertNo());
        assertThat(response.authorInfo().nickname()).isEqualTo(result.authorInfo().nickname());

        assertThat(response.themeTypes())
                .hasSize(result.themeTypes().size())
                .containsExactlyInAnyOrderElementsOf(result.themeTypes());
        assertThat(response.snapSubThemes())
                .hasSize(result.snapSubThemes().size())
                .containsExactlyInAnyOrderElementsOf(result.snapSubThemes());
        assertThat(response.moodTypes())
                .hasSize(result.moodTypes().size())
                .containsExactlyInAnyOrderElementsOf(result.moodTypes());

        assertThat(response.images())
                .hasSize(result.images().size())
                .extracting(
                        LoadPostDetailResponse.PostImageResponse::imageNo,
                        LoadPostDetailResponse.PostImageResponse::fileKey,
                        LoadPostDetailResponse.PostImageResponse::imageUrl,
                        LoadPostDetailResponse.PostImageResponse::imageOrder
                )
                .containsExactlyInAnyOrder(
                        tuple("img-1", "key-1", "url-1", 1)
                );
    }

}