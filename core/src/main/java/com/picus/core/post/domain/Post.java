package com.picus.core.post.domain;

import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class Post {

    private String postNo;

    private String authorNo;

    private String title;
    private String oneLineDescription;
    private String detailedDescription;

    // 연관된 패키지 정보를 기반으로 채워짐
    private List<String> packageNos;
    private List<PostThemeType> postThemeTypes;
    private List<SnapSubTheme> snapSubThemes;

    private List<PostMoodType> postMoodTypes;
    private SpaceType spaceType;
    private String spaceAddress;
    private Boolean isPinned;
    private List<PostImage> postImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Builder
    private Post(String postNo, String authorNo, String title, String oneLineDescription, String detailedDescription, List<String> packageNos, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                 List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress, Boolean isPinned,
                 List<PostImage> postImages, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.postNo = postNo;
        this.authorNo = authorNo;
        this.packageNos = packageNos;
        this.title = title;
        this.oneLineDescription = oneLineDescription;
        this.detailedDescription = detailedDescription;
        this.postThemeTypes = postThemeTypes == null ? Collections.emptyList() : postThemeTypes;
        this.snapSubThemes = snapSubThemes == null ? Collections.emptyList() : snapSubThemes;
        this.postMoodTypes = postMoodTypes == null ? Collections.emptyList() : postMoodTypes;
        this.spaceType = spaceType;
        this.spaceAddress = spaceAddress;
        this.isPinned = isPinned;
        this.postImages = postImages == null ? Collections.emptyList() : postImages;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        validateSnapSubThemes(); // SnapSubTheme 유효성 검증
    }

    public void updatePost(
            String title, String oneLineDescription, String detailedDescription,
            List<String> packageNos, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
            List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress
    ) {
        applyIfNotNull(title, val -> this.title = val);
        applyIfNotNull(oneLineDescription, val -> this.oneLineDescription = val);
        applyIfNotNull(detailedDescription, val -> this.detailedDescription = val);

        applyIfNotNull(packageNos, val -> this.packageNos = val);
        applyIfNotNull(postThemeTypes, val -> this.postThemeTypes = val);
        applyIfNotNull(snapSubThemes, val -> this.snapSubThemes = val);

        applyIfNotNull(postMoodTypes, val -> this.postMoodTypes = val);
        applyIfNotNull(spaceType, val -> this.spaceType = val);
        applyIfNotNull(spaceAddress, val -> this.spaceAddress = val);

        validateSnapSubThemes(); // Theme 검증
    }

    public void addPostImage(PostImage newImage) {
        // 이뮤터블 리스트 방어
        if (!(this.postImages instanceof ArrayList)) {
            this.postImages = new ArrayList<>(this.postImages);
        }

        this.postImages.add(newImage);
    }

    public void updatePostImage(PostImage updatedImage) {
        // 이뮤터블 리스트 방어
        if (!(this.postImages instanceof ArrayList)) {
            this.postImages = new ArrayList<>(this.postImages);
        }

        if (updatedImage.getPostImageNo() != null) {
            for (PostImage postImage : postImages) {
                if (updatedImage.getPostImageNo().equals(postImage.getPostImageNo())) {
                    postImage.updatePostImage(updatedImage.getFileKey(), updatedImage.getImageOrder());
                    break;
                }
            }
        }
    }

    public void deletePostImage(String deletedPostImageNo) {
        // 이뮤터블 리스트 방어
        if (!(this.postImages instanceof ArrayList)) {
            this.postImages = new ArrayList<>(this.postImages);
        }

        applyIfNotNull(deletedPostImageNo, val -> this.postImages.removeIf(
                postImage -> deletedPostImageNo.equals(postImage.getPostImageNo())
        ));
    }

    public void pin() {
        this.isPinned = true;
    }

    public void unpin() {
        this.isPinned = false;
    }

    private <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void validateSnapSubThemes() {
        boolean containsSnap = this.postThemeTypes.contains(PostThemeType.SNAP);
        boolean hasSubThemes = !this.snapSubThemes.isEmpty();

        if (containsSnap && !hasSubThemes) {
            throw new IllegalStateException("SNAP 테마가 설정되어 있으나 세부 테마(snapSubThemes)가 비어 있습니다.");
        }

        if (!containsSnap && hasSubThemes) {
            throw new IllegalStateException("SNAP 테마가 없는데 세부 테마(snapSubThemes)가 존재합니다.");
        }
    }
}
