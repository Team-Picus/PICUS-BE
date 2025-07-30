package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest;
import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest.PostImageWebReq;
import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class CreatePostWebMapperTest {

    private final CreatePostWebMapper webMapper = new CreatePostWebMapper();

    @Test
    @DisplayName("CreatePostRequest -> CreatePostCommand 매핑")
    public void toAppReq() throws Exception {
        // given
        CreatePostRequest webReq = CreatePostRequest.builder()
                .postImages(List.of(
                        PostImageWebReq.builder().fileKey("img1.jpg").imageOrder(1).build(),
                        PostImageWebReq.builder().fileKey("img2.jpg").imageOrder(2).build()
                ))
                .title("테스트 제목")
                .oneLineDescription("한 줄 설명")
                .detailedDescription("자세한 설명입니다.")
                .postThemeTypes(List.of(PostThemeType.BEAUTY, PostThemeType.EVENT))
                .postMoodTypes(List.of(PostMoodType.COZY))
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울시 강남구")
                .packageNo("pkg-001")
                .build();

        String currentUserNo = "user-123";

        // when
        CreatePostCommand appReq = webMapper.toAppReq(webReq, currentUserNo);

        // then

        assertThat(appReq.postImages()).hasSize(2)
                .extracting(PostImage::getFileKey, PostImage::getImageOrder)
                .containsExactlyInAnyOrder(
                        tuple("img1.jpg", 1),
                        tuple("img2.jpg", 2)
                );
        assertThat(appReq.title()).isEqualTo("테스트 제목");
        assertThat(appReq.oneLineDescription()).isEqualTo("한 줄 설명");
        assertThat(appReq.detailedDescription()).isEqualTo("자세한 설명입니다.");
        assertThat(appReq.postThemeTypes()).isEqualTo(List.of(PostThemeType.BEAUTY, PostThemeType.EVENT));
        assertThat(appReq.postMoodTypes()).isEqualTo(List.of(PostMoodType.COZY));
        assertThat(appReq.spaceType()).isEqualTo(SpaceType.INDOOR);
        assertThat(appReq.spaceAddress()).isEqualTo("서울시 강남구");
        assertThat(appReq.packageNo()).isEqualTo("pkg-001");
        assertThat(appReq.currentUserNo()).isEqualTo("user-123");
    }

}