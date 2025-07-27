package com.picus.core.post.domain;

import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Builder
@Getter
public class Post {

    private String postNo;

    private String authorNo;
    private String packageNo;

    private String title;
    private String oneLineDescription;
    private String detailedDescription;
    @Builder.Default
    private List<PostThemeType> postThemeTypes = new ArrayList<>();
    @Builder.Default
    private List<PostMoodType> postMoodTypes = new ArrayList<>();
    private SpaceType spaceType;
    private String spaceAddress;
    private Boolean isPinned;
    @Builder.Default
    private List<PostImage> postImages = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void updatePost(
            String title, String oneLineDescription, String detailedDescription,
            List<PostThemeType> postThemeTypes, List<PostMoodType> postMoodTypes,
            SpaceType spaceType, String spaceAddress, String packageNo
    ) {
        applyIfNotNull(title, val -> this.title = val);
        applyIfNotNull(oneLineDescription, val -> this.oneLineDescription = val);
        applyIfNotNull(detailedDescription, val -> this.detailedDescription = val);
        applyIfNotNull(postThemeTypes, val -> this.postThemeTypes = val);
        applyIfNotNull(postMoodTypes, val -> this.postMoodTypes = val);
        applyIfNotNull(spaceType, val -> this.spaceType = val);
        applyIfNotNull(spaceAddress, val -> this.spaceAddress = val);
        this.packageNo = packageNo; // packageNo는 null 가능
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

        if(updatedImage.getPostImageNo() != null) {
            for (PostImage postImage : postImages) {
                if(updatedImage.getPostImageNo().equals(postImage.getPostImageNo())) {
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

    private <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
