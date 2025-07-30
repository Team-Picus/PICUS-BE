package com.picus.core.post.domain;

import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class PostTest {

    @Test
    @DisplayName("Post를 수정한다.")
    public void updatePost_success() throws Exception {
        // given
        Post originalPost = Post.builder()
                .title("Old Title")
                .oneLineDescription("Old One Line")
                .detailedDescription("Old Description")
                .postThemeTypes(List.of(PostThemeType.BEAUTY))
                .postMoodTypes(List.of(PostMoodType.COZY))
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("Old Address")
                .packageNo("PKG001")
                .build();

        String newTitle = "New Title";
        String newOneLine = "New One Line";
        String newDetail = "New Detailed Description";
        List<PostThemeType> newThemes = List.of(PostThemeType.EVENT, PostThemeType.BEAUTY);
        List<PostMoodType> newMoods = List.of(PostMoodType.COZY, PostMoodType.MODERN);
        SpaceType newSpaceType = SpaceType.OUTDOOR;
        String newAddress = "New Address";
        String newPackageNo = null; // null 허용

        // when
        originalPost.updatePost(
                newTitle,
                newOneLine,
                newDetail,
                newThemes,
                newMoods,
                newSpaceType,
                newAddress,
                newPackageNo
        );

        // then
        assertThat(originalPost.getTitle()).isEqualTo(newTitle);
        assertThat(originalPost.getOneLineDescription()).isEqualTo(newOneLine);
        assertThat(originalPost.getDetailedDescription()).isEqualTo(newDetail);
        assertThat(originalPost.getPostThemeTypes()).containsExactlyElementsOf(newThemes);
        assertThat(originalPost.getPostMoodTypes()).containsExactlyElementsOf(newMoods);
        assertThat(originalPost.getSpaceType()).isEqualTo(newSpaceType);
        assertThat(originalPost.getSpaceAddress()).isEqualTo(newAddress);
        assertThat(originalPost.getPackageNo()).isNull(); // null로 잘 변경되었는지 확인
    }

    @Test
    @DisplayName("Post 수정 시 null 값이 전달되면 기존 값이 유지된다.")
    void updatePost_withNullValues_shouldKeepOriginal() {
        // given
        Post originalPost = Post.builder()
                .title("Original Title")
                .oneLineDescription("Original One Line")
                .detailedDescription("Original Detail")
                .postThemeTypes(List.of(PostThemeType.BEAUTY))
                .postMoodTypes(List.of(PostMoodType.COZY))
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("Original Address")
                .packageNo("PKG001")
                .build();

        // when - 모든 값에 null을 넣음
        originalPost.updatePost(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "UPDATED-PACKAGE" // packageNo는 null 가능하므로 변경 허용
        );

        // then - 나머지는 유지, packageNo만 변경
        assertThat(originalPost.getTitle()).isEqualTo("Original Title");
        assertThat(originalPost.getOneLineDescription()).isEqualTo("Original One Line");
        assertThat(originalPost.getDetailedDescription()).isEqualTo("Original Detail");
        assertThat(originalPost.getPostThemeTypes()).containsExactly(PostThemeType.BEAUTY);
        assertThat(originalPost.getPostMoodTypes()).containsExactly(PostMoodType.COZY);
        assertThat(originalPost.getSpaceType()).isEqualTo(SpaceType.INDOOR);
        assertThat(originalPost.getSpaceAddress()).isEqualTo("Original Address");
        assertThat(originalPost.getPackageNo()).isEqualTo("UPDATED-PACKAGE");
    }

    @Test
    @DisplayName("Post의 PostImage를 추가한다.")
    public void addPostImage_success() throws Exception {
        // given
        Post post = Post.builder()
                .postImages(
                        List.of(PostImage.builder()
                                .fileKey("img1.jpg")
                                .imageOrder(1)
                                .build())
                )
                .build();

        PostImage newImage = PostImage.builder()
                .fileKey("img2.jpg")
                .imageOrder(2)
                .build();

        // when
        post.addPostImage(newImage);

        // then
        assertThat(post.getPostImages()).hasSize(2)
                .extracting(
                        PostImage::getFileKey,
                        PostImage::getImageOrder
                ).containsExactlyInAnyOrder(
                        tuple("img1.jpg", 1),
                        tuple("img2.jpg", 2)
                );
    }

    @Test
    @DisplayName("Post의 PostImage를 수정한다.")
    public void updatePostImage_success() throws Exception {
        // given
        Post post = Post.builder()
                .postImages(
                        List.of(PostImage.builder()
                                .postImageNo("i-123")
                                .fileKey("img1.jpg")
                                .imageOrder(1)
                                .build())
                )
                .build();

        PostImage newImage = PostImage.builder()
                .postImageNo("i-123")
                .fileKey("img2.jpg")
                .imageOrder(2)
                .build();

        // when
        post.updatePostImage(newImage);

        // then
        assertThat(post.getPostImages()).hasSize(1)
                .extracting(
                        PostImage::getPostImageNo,
                        PostImage::getFileKey,
                        PostImage::getImageOrder
                ).containsExactlyInAnyOrder(
                        tuple("i-123", "img2.jpg", 2)
                );
    }

    @Test
    @DisplayName("Post의 PostImage를 삭제한다.")
    public void deletePostImage_success() throws Exception {
        // given
        Post post = Post.builder()
                .postImages(
                        List.of(PostImage.builder()
                                .postImageNo("i-123")
                                .fileKey("img1.jpg")
                                .imageOrder(1)
                                .build())
                )
                .build();

        // when
        post.deletePostImage("i-123");

        // then
        assertThat(post.getPostImages()).isEmpty();
    }

    @Test
    @DisplayName("Post의 PostImage를 삭제할 때 해당되는 imageNo가 없으면 삭제되지 않는다.")
    public void deletePostImage_if_imageNoNotExist() throws Exception {
        // given
        Post post = Post.builder()
                .postImages(
                        List.of(PostImage.builder()
                                .postImageNo("i-123")
                                .fileKey("img1.jpg")
                                .imageOrder(1)
                                .build())
                )
                .build();

        // when
        post.deletePostImage("i-120");

        // then
        assertThat(post.getPostImages()).isNotEmpty();
    }

    @Test
    @DisplayName("Post를 고정처리한다.")
    public void pin() throws Exception {
        // given
        Post post = Post.builder()
                .isPinned(false)
                .build();

        // when
        post.pin();

        // then
        assertThat(post.getIsPinned()).isTrue();
    }
    @Test
    @DisplayName("Post를 고정해제한다.")
    public void unpin() throws Exception {
        // given
        Post post = Post.builder()
                .isPinned(true)
                .build();

        // when
        post.unpin();

        // then
        assertThat(post.getIsPinned()).isFalse();
    }

}