package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest.PostImageRequest;
import com.picus.core.post.application.port.in.command.UpdatePostCommand;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.picus.core.post.application.port.in.command.ChangeStatus.*;
import static com.picus.core.post.application.port.in.command.ChangeStatus.NEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UpdatePostWebMapperTest {

    private UpdatePostWebMapper webMapper = new UpdatePostWebMapper();

    @Test
    @DisplayName("UpdatePostRequest -> UpdatePostCommand 매핑")
    public void toCommand_success() throws Exception {
        // given
        UpdatePostRequest webReq = UpdatePostRequest.builder()
                .postImages(List.of(
                        PostImageRequest.builder().fileKey("img1.jpg").imageOrder(1).changeStatus(NEW).build(),
                        PostImageRequest.builder().postImageNo("img-123").fileKey("img2.jpg").imageOrder(2).changeStatus(UPDATE).build()
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

        String postNo = "post-123";
        String currentUserNo = "user-123";

        // when
        UpdatePostCommand appReq = webMapper.toCommand(webReq, postNo, currentUserNo);

        // then
        assertThat(appReq.postNo()).isEqualTo("post-123");
        assertThat(appReq.title()).isEqualTo("테스트 제목");
        assertThat(appReq.oneLineDescription()).isEqualTo("한 줄 설명");
        assertThat(appReq.detailedDescription()).isEqualTo("자세한 설명입니다.");
        assertThat(appReq.postThemeTypes()).containsExactly(PostThemeType.BEAUTY, PostThemeType.EVENT);
        assertThat(appReq.postMoodTypes()).containsExactly(PostMoodType.COZY);
        assertThat(appReq.spaceType()).isEqualTo(SpaceType.INDOOR);
        assertThat(appReq.spaceAddress()).isEqualTo("서울시 강남구");
        assertThat(appReq.packageNo()).isEqualTo("pkg-001");
        assertThat(appReq.currentUserNo()).isEqualTo("user-123");

        // 이미지 목록 검증
        assertThat(appReq.postImages()).hasSize(2);

        assertThat(appReq.postImages()).extracting(
                UpdatePostCommand.UpdatePostImageCommand::fileKey,
                UpdatePostCommand.UpdatePostImageCommand::imageOrder,
                UpdatePostCommand.UpdatePostImageCommand::changeStatus
        ).containsExactly(
                tuple("img1.jpg", 1, NEW),
                tuple("img2.jpg", 2, UPDATE)
        );
    }

}